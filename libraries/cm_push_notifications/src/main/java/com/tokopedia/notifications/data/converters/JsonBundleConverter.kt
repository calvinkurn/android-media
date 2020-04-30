package com.tokopedia.notifications.data.converters

import android.os.Bundle
import com.google.gson.reflect.TypeToken
import com.tokopedia.graphql.CommonUtils.fromJson
import com.tokopedia.notifications.common.PayloadConverter

object JsonBundleConverter {

    private val mapType by lazy(LazyThreadSafetyMode.NONE) {
        object : TypeToken<Map<String?, String>>() {}.type
    }

    private fun mapToBundle(map: Map<String, String>): Bundle {
        return PayloadConverter.convertMapToBundle(map)
    }

    fun jsonToBundle(json: String): Bundle {
        val toMap = fromJson<Map<String, String>>(json, mapType)
        return mapToBundle(toMap)
    }

}