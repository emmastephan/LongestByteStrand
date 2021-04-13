//import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.nio.file.Files;

/** Main class of the Longest Byte Strand program.
 * @author Emma Stephan
 */
public class Main {
    /** Current Longest Common Byte Strand Length */
    public static int length = 0;

    /** Hashmap of dictionaries containing the names of files and their
     * corresponding array of bytes
     */
    public static HashMap<String,byte[]> dictionaries = new HashMap<String,byte[]>();

    /** Map containing the names of files that contain the LCBS and their
     * corresponding offset positions
     */
    public static Map<String,Integer> positions = new HashMap<String,Integer>();

    /** Function that finds the longest common byte sequence in two byte arrays
     * and return the length of it and the offset position of the sequence
     * in the first file
     */
    private static int[] LCByteStr(byte[] X, byte[] Y, int m, int n) {
        // Variable to store length of LCBS
        int result = 0;

        // Variable to store ending point of
        // LCBS in X.
        int end = 0;

        // Matrix to store result of two
        // consecutive rows at a time.
        int len[][] = new int[m+1][n + 1];

        // For a particular value of i and j,
        // len[i][j] stores length of longest
        // common strand in String X[0..i] and Y[0..j].
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                System.out.println(m);
                System.out.println(n);
                System.out.println(i);
                System.out.println(j);
                if (i == 0 || j == 0) {
                    len[i][j] = 0;
                } else if (X[i - 1] == Y[j - 1]) {
                    len[i][j] = len[i - 1][j - 1] + 1;
                    if (len[i][j] > result) {
                        result = len[i][j];
                        end = i - 1;
                    }
                } else {
                    len[i][j] = 0;
                }
            }
        }

        // If there is no common strand, return length -1.
        if (result == 0) {
            return new int[] {-1};
        }

        // LCBS is from index end - result + 1 to index end in X.
        return new int[] {result, end - result + 1};
    }

    /** The main driver code for the Longest Byte Strand program.
     * ARGS are a list of files
     */
    public static void main(String[] args) throws IOException {
        Hashtable<String, Boolean> bools = new Hashtable<String, Boolean>();
        for (String fileName : args) {
            File file = new File(fileName);
            byte[] bytes = Files.readAllBytes(file.toPath());
            dictionaries.put(fileName, bytes);
            bools.put(fileName, Boolean.FALSE);
        }

        for (String fileName1 : args) {
            for (String fileName2: args) {
                if (fileName1 != fileName2 && !bools.get(fileName2)) {
                    int m = dictionaries.get(fileName1).length;
                    int n = dictionaries.get(fileName2).length;
                    int[] newLCS = LCByteStr(dictionaries.get(fileName1), dictionaries.get(fileName2), m, n);
                    if (newLCS[0] > length) {
                        length = newLCS[0];
                        positions.clear();
                        positions.put(fileName1, newLCS[1]);
                    }
                }
            }
            bools.replace(fileName1, Boolean.TRUE);
        }

        for (String fileName : dictionaries.keySet()) {
            if (length != 0) {
                if (!positions.containsKey(fileName)) {
                    for (int j = 0; j < dictionaries.get(fileName).length - length; j++) {
                        for (int i = 0; i < length; i++) {
                            Iterator<String> iterator = positions.keySet().iterator();
                            String key = iterator.next();
                            if (i >= dictionaries.get(fileName).length ||
                                    (dictionaries.get(key)[positions.get(key) + i] != dictionaries.get(fileName)[i])) {
                                break;
                            } else if (i == length - 1) {
                                positions.put(fileName, j);
                            }
                        }
                    }
                }
            }
        }
        System.out.println(length);
        for (String key : positions.keySet()) {
            System.out.println(key + " offset: " + positions.get(key));
        }
    }
}
