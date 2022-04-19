package com.tokopedia.analyticsdebugger.cassava.core

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.tokopedia.analyticsdebugger.database.GtmLogDB

internal typealias JsonRegexPair = Pair<Int, Map<String, Any>>

internal typealias JsonMap = Map<String, Any>

fun JsonRegexPair.toDefaultValidator() = Validator(this.second, id = this.first)

fun JsonMap.toDefaultValidator() = Validator(this)

/**
 * Prefer to use [AnalyticsMapParser] if possible
* */
fun String.toJsonMap(): JsonMap {
    val jsonType = object : TypeToken<Map<String, Any>>() {}.type
    return try {
        Gson().fromJson(this, jsonType)
    } catch (e: JsonSyntaxException) {
        emptyMap()
    }
}

internal fun GtmLogDB.toUiModel() = GtmLogUi(
        this.id, this.data, this.name, this.timestamp
)
