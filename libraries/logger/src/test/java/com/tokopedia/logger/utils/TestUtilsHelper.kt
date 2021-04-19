package com.tokopedia.logger.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.File

object TestUtilsHelper {

    fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

    inline fun <reified T> parseJson(json: String): T {
        return Gson().fromJson(json, T::class.java)
    }

    inline fun<reified T: Any> createSuccessResponse(jsonPath: String): T {
        val mockJsonResponse = getJsonFromFile(jsonPath)
        val response: JsonObject = parseJson(mockJsonResponse)
        val jsonData = response.toString()
        val data: T = parseJson(jsonData)
        return data
    }

    fun getPriority(tagPriority: String): Int {
        if (tagPriority == LoggerReporting.TAG_OFFLINE) {
            return LoggerReporting.PRIORITY_OFFLINE
        }
        return LoggerReporting.PRIORITY_ONLINE
    }
}