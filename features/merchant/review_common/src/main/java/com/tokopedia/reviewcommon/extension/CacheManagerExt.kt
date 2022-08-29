package com.tokopedia.reviewcommon.extension

import com.google.gson.Gson
import com.tokopedia.cachemanager.CacheManager
import java.lang.reflect.Type

fun <T> CacheManager.get(customId: String, type: Type, defaultValue: T? = null, gson: Gson): T? {
    return try {
        val jsonString: String? = cacheRepository.get(id + customId)
        if (jsonString.isNullOrEmpty()) {
            defaultValue
        } else {
            gson.fromJson(jsonString, type)
        }
    } catch (e: Throwable) {
        defaultValue
    }
}

fun CacheManager.put(customId: String, objectToPut: Any?, cacheDuration: Long = CacheManager.defaultExpiredDuration, gson: Gson) {
    if (objectToPut == null) {
        return
    }
    try {
        cacheRepository.put(id + customId,
            gson.toJson(objectToPut),
            cacheDuration)
    } catch (e: Throwable) {
        return
    }
}