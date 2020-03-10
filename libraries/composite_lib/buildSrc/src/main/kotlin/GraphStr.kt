import com.tokopedia.plugin.Bimap
import java.util.*
import kotlin.collections.ArrayList

class GraphStr {
    private val adj: ArrayList<ArrayList<String>> = ArrayList()
    val listTop = mutableListOf<String>()

    private val verticesBimap = Bimap()

    // Function to add an edge into the graph
    internal fun addEdge(v: String, w: String) {
        adj[verticesBimap.getIndex(v)].add(w)
    }

    internal fun addVertices(vertice: String) {
        if (verticesBimap.getIndex(vertice) == -1) {
            verticesBimap.addString(vertice)
            adj.add(ArrayList())
        }
    }

    // A recursive function used by topologicalSort
    private fun topologicalSortUtil(v: Int, visited: BooleanArray, stack: Stack<String>) {
        // Mark the current node as visited.
        visited[v] = true
        // Recur for all the vertices adjacent to this vertex
        for (str in adj[v]) {
            val index = verticesBimap.getIndex(str)
            if (!visited[index]) topologicalSortUtil(index, visited, stack)
        }
        // Push current vertex to stack which stores result
        stack.push(verticesBimap.getString(v))
    }

    // The function to do Topological Sort. It uses recursive topologicalSortUtil()
    internal fun topologicalSort() {
        val stack = Stack<String>()
        val verticesSize = verticesBimap.bimapSize
        // Mark all the vertices as not visited
        val visited = BooleanArray(verticesSize)
        for (i in 0 until verticesSize) {
            visited[i] = false
        }
        for (i in 0 until verticesSize) {
            if (!visited[i]) {
                topologicalSortUtil(i, visited, stack)
            }
        }
        // Print contents of stack
        while (!stack.empty()) {
            val element = stack.pop()
            listTop.add(element)
        }
    }

}