package com.tokopedia.tokopedianow.common.util

import com.google.gson.Gson
import java.lang.Exception

object JsonUtil {
    fun convertJsonStringToMap(jsonString: String): Map<String, String> {
        var map: Map<String, String> = HashMap()
        map = try {
            Gson().fromJson(jsonString, map.javaClass)
        } catch (e: Exception) {
            mapOf()
        }
        return map
    }
}