package com.tokopedia.topchat

import androidx.collection.ArrayMap
import com.tokopedia.topchat.common.network.TopchatCacheManager
import java.lang.reflect.Type

class FakeTopchatCacheManager(
    var cache: Any? = null
) : TopchatCacheManager {

    val cacheMap: ArrayMap<Any, Any> = ArrayMap()
    var throwError: Boolean = false
    var isPreviousLoadSuccess: Boolean = true

    override fun saveCache(key: String, obj: Any) {
        cacheMap[key] = obj
    }

    override fun <T> loadCache(key: String, type: Type): T {
        if (throwError) {
            throw Exception()
        }
        if (cache != null) {
            return cache as T
        }
        return cacheMap[key] as T
    }

    override fun saveState(stateCacheKey: String, value: Boolean) {
        cacheMap[stateCacheKey] = value
    }

    override fun getPreviousState(stateCacheKey: String, defaultValue: Boolean): Boolean {
        return cacheMap[stateCacheKey] as? Boolean ?: isPreviousLoadSuccess
    }

    override fun saveLongCache(cacheKey: String, value: Long) {
        cacheMap[cacheKey] = value
    }

    override fun getLongCache(cacheKey: String, defaultValue: Long): Long {
        return cacheMap[cacheKey] as? Long ?: defaultValue
    }
}
