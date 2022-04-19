package com.tokopedia.test.application.environment.interceptor.mock

data class MockKey(
    val query: String,
    val variables: Map<String, String>
) {
    // expectation #1: "query queryname"
    // expectation #2: "\"operator\": \"18\""
    fun inList(): List<String> {
        val keys = mutableListOf<String>()
        keys.add("query $query")
        variables.forEach {
            keys.add("\"${it.key}\": \"${it.value}\"")
        }

        return keys
    }
}