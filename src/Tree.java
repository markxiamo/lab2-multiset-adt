import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tree {

    // The item stored at the root.
    // null means the tree is empty.
    private Integer root;

    // The list of all subtrees.
    private List<Tree> subtrees;

    private static final Random RANDOM = new Random();

    /**
     * Initialize an empty Tree.
     */
    public Tree() {
        this.root = null;
        this.subtrees = new ArrayList<>();
    }

    /**
     * Initialize a Tree with the given root and subtrees.
     *
     * @param root the root item
     * @param subtrees the subtrees
     */
    public Tree(int root, List<Tree> subtrees) {
        this.root = root;
        this.subtrees = new ArrayList<>(subtrees);
    }

    /**
     * Return whether this tree is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Return the number of items in this tree.
     *
     * @return the size of this tree
     */
    public int size() {
        if (isEmpty()) {
            return 0;
        }

        int size = 1;

        for (Tree subtree : subtrees) {
            size += subtree.size();
        }

        return size;
    }

    /**
     * Return the number of occurrences of item in this tree.
     *
     * @param item the item to count
     * @return the number of occurrences
     */
    public int count(int item) {
        if (isEmpty()) {
            return 0;
        }

        int num = 0;

        if (root == item) {
            num++;
        }

        for (Tree subtree : subtrees) {
            num += subtree.count(item);
        }

        return num;
    }

    /**
     * Return whether item is in this tree.
     *
     * @param item the item to check
     * @return true if item is in the tree
     */
    public boolean contains(int item) {
        if (isEmpty()) {
            return false;
        }

        if (root == item) {
            return true;
        }

        for (Tree subtree : subtrees) {
            if (subtree.contains(item)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Return a string representation of this tree.
     *
     * @return the tree as a string
     */
    @Override
    public String toString() {
        return strIndented(0);
    }

    /**
     * Return an indented string representation of this tree.
     *
     * @param depth indentation depth
     * @return formatted string
     */
    private String strIndented(int depth) {
        if (isEmpty()) {
            return "";
        }

        StringBuilder s = new StringBuilder();

        s.append("  ".repeat(depth));
        s.append(root);
        s.append("\n");

        for (Tree subtree : subtrees) {
            s.append(subtree.strIndented(depth + 1));
        }

        return s.toString();
    }

    /**
     * Return the average of all values in this tree.
     *
     * @return average, or 0.0 if empty
     */
    public double average() {
        if (isEmpty()) {
            return 0.0;
        }

        int[] result = averageHelper();

        return (double) result[0] / result[1];
    }

    /**
     * Return total and size of this tree.
     *
     * @return an array containing total and size
     */
    private int[] averageHelper() {
        if (isEmpty()) {
            return new int[]{0, 0};
        }

        int total = root;
        int size = 1;

        for (Tree subtree : subtrees) {
            int[] result = subtree.averageHelper();

            total += result[0];
            size += result[1];
        }

        return new int[]{total, size};
    }

    /**
     * Return whether this tree is equal to another object.
     *
     * @param other the object to compare
     * @return true if the trees are equal
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Tree)) {
            return false;
        }

        Tree otherTree = (Tree) other;

        if (isEmpty() && otherTree.isEmpty()) {
            return true;
        }

        if (isEmpty() || otherTree.isEmpty()) {
            return false;
        }

        if (!root.equals(otherTree.root)) {
            return false;
        }

        return subtrees.equals(otherTree.subtrees);
    }

    /**
     * Return a list of all leaf items in the tree.
     *
     * @return list of leaf items
     */
    public List<Integer> leaves() {
        List<Integer> leaves = new ArrayList<>();

        if (isEmpty()) {
            return leaves;
        }

        if (subtrees.isEmpty()) {
            leaves.add(root);
            return leaves;
        }

        for (Tree subtree : subtrees) {
            leaves.addAll(subtree.leaves());
        }

        return leaves;
    }

    /**
     * Delete one occurrence of item from this tree.
     *
     * @param item the item to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteItem(int item) {
        if (isEmpty()) {
            return false;
        }

        if (root == item) {
            deleteRoot();
            return true;
        }

        for (Tree subtree : new ArrayList<>(subtrees)) {
            boolean deleted = subtree.deleteItem(item);

            if (deleted && subtree.isEmpty()) {
                subtrees.remove(subtree);
                return true;
            } else if (deleted) {
                return true;
            }
        }

        return false;
    }

    /**
     * Delete the root item.
     */
    private void deleteRoot() {
        if (subtrees.isEmpty()) {
            root = null;
        } else {
            Tree chosenSubtree = subtrees.remove(subtrees.size() - 1);

            root = chosenSubtree.root;
            subtrees.addAll(chosenSubtree.subtrees);
        }
    }

    /**
     * Insert item into this tree.
     *
     * @param item the item to insert
     */
    public void insert(int item) {
        if (isEmpty()) {
            root = item;
        } else if (subtrees.isEmpty()) {
            subtrees.add(new Tree(item, new ArrayList<>()));
        } else {
            if (RANDOM.nextInt(3) == 2) {
                subtrees.add(new Tree(item, new ArrayList<>()));
            } else {
                int subtreeIndex = RANDOM.nextInt(subtrees.size());
                subtrees.get(subtreeIndex).insert(item);
            }
        }
    }

    /**
     * Insert item as a child of parent.
     *
     * @param item the item to insert
     * @param parent the parent item
     * @return true if successful, false otherwise
     */
    public boolean insertChild(int item, int parent) {
        if (isEmpty()) {
            return false;
        }

        if (root == parent) {
            subtrees.add(new Tree(item, new ArrayList<>()));
            return true;
        }

        for (Tree subtree : subtrees) {
            if (subtree.insertChild(item, parent)) {
                return true;
            }
        }

        return false;
    }
}