package com.tokopedia.notifications.data.converters

import android.os.Bundle
import com.google.gson.reflect.TypeToken
import com.tokopedia.graphql.CommonUtils.fromJson
import com.tokopedia.notifications.common.CMConstant.PayloadKeys.*
import com.tokopedia.notifications.common.PayloadConverter

object JsonBundleConverter {

    private const val HOURS_24_IN_MILLIS = 24 * 60 * 60 * 1000L
    private const val DAYS_7 = HOURS_24_IN_MILLIS * 7

    private val mapType by lazy(LazyThreadSafetyMode.NONE) {
        object : TypeToken<Map<String?, String>>() {}.type
    }

    private fun setOfflineData(): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            put(NOTIFICATION_MODE, "true")
            put(NOTIFICATION_START_TIME, System.currentTimeMillis().toString())
            put(NOTIFICATION_END_TIME, (System.currentTimeMillis() + DAYS_7).toString())
        }
    }

    fun jsonToBundle(json: String): Bundle {
        val toMap = fromJson<Map<String, String>>(json, mapType).toMutableMap()
        toMap.putAll(setOfflineData())
        return PayloadConverter.convertMapToBundle(toMap)
    }

}