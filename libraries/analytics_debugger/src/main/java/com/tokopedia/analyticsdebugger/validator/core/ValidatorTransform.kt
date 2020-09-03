package com.tokopedia.analyticsdebugger.validator.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.validator.Utils

internal typealias JsonMap = Map<String, Any>

internal fun JsonMap.toDefaultValidator() = Validator(
        Utils.getAnalyticsName(this),
        this
)

internal fun JsonMap.toJson(): String =
        GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(this)

internal fun String.toJsonMap(): JsonMap {
    val jsonType = object : TypeToken<Map<String, Any>>() {}.type
    return Gson().fromJson(this, jsonType)
}

internal fun GtmLogDB.toUiModel() = GtmLogUi(
        this.id, this.data, this.name, this.category, this.timestamp
)

internal fun Map<String, Any>.getQueryMap(): List<Map<String, Any>> {
    return this["query"] as List<Map<String, Any>>
}

internal fun Map<String, Any>.getMode(): String = this["mode"] as String