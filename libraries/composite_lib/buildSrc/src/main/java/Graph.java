import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Graph {
    private int V;   // No. of vertices 
    private ArrayList<ArrayList<Integer>> adj; // Adjacency List
    private ArrayList<Integer> listTop = new ArrayList<>();

    //Constructor 
    Graph(int v) {
        V = v;
        adj = new ArrayList<>();
        for (int i = 0; i < v; ++i)
            adj.add(new ArrayList<>());
    }

    public List<Integer> getListTop() {
        return listTop;
    }

    // Function to add an edge into the graph
    void addEdge(int v, int w) {
        adj.get(v).add(w);
    }

    // A recursive function used by topologicalSort 
    private void topologicalSortUtil(int v, boolean[] visited, Stack<Integer> stack) {
        // Mark the current node as visited. 
        visited[v] = true;
        Integer i;

        // Recur for all the vertices adjacent to this 
        // vertex 
        for (Integer integer : adj.get(v)) {
            i = integer;
            if (!visited[i])
                topologicalSortUtil(i, visited, stack);
        }

        // Push current vertex to stack which stores result 
        stack.push(v);
    }

    // The function to do Topological Sort. It uses 
    // recursive topologicalSortUtil() 
    void topologicalSort() {
        Stack<Integer> stack = new Stack<Integer>();

        // Mark all the vertices as not visited 
        boolean[] visited = new boolean[V];
        for (int i = 0; i < V; i++) {
            visited[i] = false;
        }

        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                topologicalSortUtil(i, visited, stack);
            }
        }

        // Print contents of stack 
        while (!stack.empty()) {
            Integer element = (Integer) stack.pop();
            listTop.add(element);
        }
    }
} 