package com.tokopedia.analyticsdebugger.validator.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.validator.Utils

typealias JsonMap = Map<String, Any>

fun JsonMap.toDefaultValidator() = Validator(
        Utils.getAnalyticsName(this),
        this
)

fun JsonMap.toJson(): String =
        GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(this)

fun String.toJsonMap(): JsonMap {
    val jsonType = object : TypeToken<Map<String, Any>>() {}.type
    return Gson().fromJson(this, jsonType)
}

fun GtmLogDB.toUiModel() = GtmLogUi(
        this.id, this.data, this.name, this.category, this.timestamp
)