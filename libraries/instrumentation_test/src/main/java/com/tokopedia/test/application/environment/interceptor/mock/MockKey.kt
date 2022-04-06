package com.tokopedia.test.application.environment.interceptor.mock

import java.lang.StringBuilder

/* Proto #2 */
data class MockKey(
    val query: String,
    val variables: Map<String, String>
) {
    // expectation: "\"operator\": \"18\""
    fun inList(): List<String> {
        val keys = mutableListOf<String>()
        keys.add(query)
        variables.forEach {
            keys.add("\"${it.key}\": \"${it.value}\"")
        }
        return keys
    }
}