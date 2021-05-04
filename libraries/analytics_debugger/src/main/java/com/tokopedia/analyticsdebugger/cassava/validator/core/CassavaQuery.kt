package com.tokopedia.analyticsdebugger.cassava.validator.core

import com.google.gson.reflect.TypeToken
import com.tokopedia.analyticsdebugger.cassava.validator.Utils

/**
 * mode : Mode of Query Validator
 * query : List of Query,
 *          the first item is query data layer id,
 *          the second item is query regex
 * readme : Readme of the journey
 */
data class CassavaQuery(
        val mode: QueryMode,
        val query: List<Pair<Int, Map<String, Any>>>,
        val readme: String?
)

enum class QueryMode(val value: String) {
    EXACT("exact"), SUBSET("subset");

    companion object {
        fun from(v: String) = values().first { it.value == v }
    }
}

fun String.toCassavaQuery(): CassavaQuery {
    val parser = Utils.provideJsonParser()
    val jsonType = object : TypeToken<Map<String, Any>>() {}.type
    val jsonMap: Map<String, Any> = parser.fromJson(this, jsonType)

    val query: List<Map<String, Any>> = jsonMap["query"] as? List<Map<String, Any>>
            ?: throw QueryTestParseException("Error while parsing the query")

    return CassavaQuery(
            mode = QueryMode.from(jsonMap["mode"] as? String ?: "exact"),
            query = query.map {
                Pair(0, it)
            }.toList(),
            readme = jsonMap["readme"] as? String
    )
}