package com.tokopedia.analyticsdebugger.cassava.validator.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.tokopedia.analyticsdebugger.database.GtmLogDB

internal typealias JsonMap = Map<String, Any>

fun JsonMap.toDefaultValidator() = Validator(this)

fun String.toJsonMap(): JsonMap {
    val jsonType = object : TypeToken<Map<String, Any>>() {}.type
    return try {
        Gson().fromJson(this, jsonType)
    } catch (e: JsonSyntaxException) {
        emptyMap()
    }
}

internal fun JsonMap.toJson(): String =
        GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(this)

internal fun GtmLogDB.toUiModel() = GtmLogUi(
        this.id, this.data, this.name, this.timestamp
)
