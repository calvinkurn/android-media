package com.tokopedia.tokochat.stub.common

import androidx.collection.ArrayMap
import com.tokopedia.tokochat.common.util.TokoChatCacheManager

class TokoChatCacheManagerStub : TokoChatCacheManager {

    private val cacheMap: ArrayMap<Any, Any> = ArrayMap()
    var throwError: Boolean = false

    override fun saveCache(key: String, obj: Any) {
        cacheMap[key] = obj
    }

    override fun <T> loadCache(key: String, type: Class<T>): T? {
        if (throwError) {
            throw Exception()
        }
        return cacheMap[key] as T
    }

    override fun saveState(stateCacheKey: String, value: Boolean) {
        cacheMap[stateCacheKey] = value
    }

    override fun getPreviousState(stateCacheKey: String): Boolean {
        return cacheMap[stateCacheKey] as? Boolean ?: false
    }

    fun resetAll() {
        cacheMap.clear()
    }
}
