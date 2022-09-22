package com.tokopedia.macrobenchmark_util.env.mock

/**
 * MockKey is a GQL request details wrapper, currently used in MockInterceptor
 */
data class MockKey(
    val query: String,
    val variables: Map<String, String>
) {
    /**
     * Content example:
     * query: "queryname" or "mutationname"
     * variables: "\"operator\": \"18\""
     * */
    fun inList(): List<String> {
        return mutableListOf<String>().apply {
            add(query)
            variables.forEach {
                add("\"${it.key}\": \"${it.value}\"")
            }
        }
    }
}