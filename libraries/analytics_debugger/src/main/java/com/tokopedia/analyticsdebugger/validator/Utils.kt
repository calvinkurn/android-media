package com.tokopedia.analyticsdebugger.validator

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException


internal object Utils {

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun getAnalyticsName(item: Map<String, Any>): String {
        return if (item.containsKey("event")) item["event"] as String
        else if (item.isNotEmpty()) item[item.keys.first()] as String
        else ""
    }

    fun jsonToMap(s: String): Map<String, Any> {
        val jsonType = object : TypeToken<Map<String, Any>>() {}.type
        return Gson().fromJson(s, jsonType)
    }
}