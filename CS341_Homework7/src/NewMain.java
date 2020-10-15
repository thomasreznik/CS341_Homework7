import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NewMain {
	public static int totallinecount;
	public static int countcomments;
	public static int countblanklines;
	public static int finallinecount;
	public static HashMap<Character, Integer> valueHashMap = new HashMap<>();
	public static HashMap<String, Integer> keyCount = new HashMap<>();
	public static String filePath = "";
	// Decided to use ALL of the Java Keywords to search for
	public static String[] keywordArr = { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
			"class", "continue", "const", "default", "do", "double", "else", "enum", "exports", "extends", "final",
			"finally", "float", "for", "goto", "if", "implements", "imports", "instanced", "int", "interface", "long",
			"module", "native", "new", "package", "private", "protected", "public", "requires", "return", "short",
			"static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try",
			"var", "void", "volatile", "while" };

	public static void main(String[] args) {

		Stopwatch sw = new Stopwatch();
		// starting stopwatch
		sw.start();
		CountLetters();
		readFile();
		lineCounter();
		System.out.println();
		keyCount = sortNumerical(keyCount);
		// stopping stopwatch
		sw.stop();
		System.out.println("Sorted Java keywords by number of occurances in file: " + keyCount.toString());

		System.out.println();
		System.out.println("Number of total lines in the file: " + totallinecount); // Print the line count
		System.out.println("Number of lines with comments: " + countcomments); // Print number of lines of code with
																				// comments
		System.out.println("Number of blank lines: " + countblanklines); // Print number of lines of code with
																			// comments
		finallinecount = totallinecount - countcomments - countblanklines;
		System.out.println("Number of lines of code without comments and blank lines: " + finallinecount);
		System.out.println();
		System.out.println("The program took " + sw.getElapsedTime() + " milliseconds to run ");
	}

	// This method puts the letters in the valueHashMap

	public static void CountLetters() {

		for (int i = 0; i < keywordArr.length; i++) {
			char first = keywordArr[i].charAt(0);
			char last = keywordArr[i].charAt(keywordArr[i].length() - 1);

// to check if map has value of first 
			if (!valueHashMap.containsKey(first)) {
				valueHashMap.put(first, 1);
			} else {
				valueHashMap.put(first, valueHashMap.get(first) + 1);
			}

// to check if map has values of last

			if (!valueHashMap.containsKey(last)) {
				valueHashMap.put(last, 1);
			} else {
				valueHashMap.put(last, valueHashMap.get(last) + 1);
			}

		}

// sort the list by highest values

		valueHashMap = sortNumerical(valueHashMap);

	}

	// Reads in a file

	public static void readFile() {
		filePath = chooseFile();
		System.out.println();
	}

	// GUI for JfileChooser
	public static String chooseFile() {

		JButton open = new JButton();
		JFileChooser fc = new JFileChooser();

		FileNameExtensionFilter filter = new FileNameExtensionFilter("JAVA FILES", "java", "java", "TEXT FILES", "txt",
				"text");
		fc.setFileFilter(filter);
		fc.setCurrentDirectory(new java.io.File("/Users/thomasreznik/eclipse-workspace/"));
		fc.setDialogTitle("Select a File: ");
		if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile().getAbsolutePath();
		} else {

			return "selected cancel";
		}
	}

	// SORTING IN NUMERICAL ORDER
	private static HashMap sortNumerical(HashMap map) {

		List<?> list = new LinkedList(map.entrySet());

		// Defined Comparator here for sorting purposes

		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		// Using LinkedHashMap in order to store the insertion order that we have

		HashMap sortedHashMap = new LinkedHashMap();
		for (java.util.Iterator it = list.iterator(); ((java.util.Iterator) it).hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		return sortedHashMap;
	}

	public static void lineCounter() {
		totallinecount = 0;
		countcomments = 0;
		countblanklines = 0;
		finallinecount = 0;

		if (!filePath.equals("selected cancel")) {

			System.out.println("File path: " + filePath);

			LineNumberReader reader = null;

			try {

				reader = new LineNumberReader(new FileReader(new File(filePath)));
				String str;

				while ((str = reader.readLine()) != null) {

					str = str.replaceAll("\\s+", "");
					totallinecount++;

					if (str.equals("")) {
						countblanklines++;
					}

					if ((str.length() >= 2) && (str.startsWith("//"))) {
						countcomments++;
					} else if (str.contains("/*")) {
						countcomments++;
						while (((str = reader.readLine()) != null) && !(str.endsWith("*/"))) {
							totallinecount++;
							countcomments++;

						}
						totallinecount++;
						countcomments++;

					}

					for (int i = 0; i < keywordArr.length; i++) {

						// If the Key is not present in file, the value that it is assigned is 0
						if (!keyCount.containsKey(keywordArr[i])) {
							keyCount.put(keywordArr[i], 0);
						}
						// Check if exists in line
						if (str.contains(keywordArr[i])) {
							// If Key is present, value increases by 1
							if (keyCount.containsKey(keywordArr[i])) {
								keyCount.put(keywordArr[i], keyCount.get(keywordArr[i]) + 1);
								// Initially sets up value for the found Key as 1
							} else {
								keyCount.put(keywordArr[i], 1);
							}

						}

					}

				}
				System.out.println();
				// Prints out the unsorted key value pairs
				System.out.println("Counting Values for each key, being the Java keywords, within file you chose : "
						+ keyCount.toString());
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		} else {
			System.out.println("You have left the Gui App");
		}

	}
}