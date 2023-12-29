package com.tokopedia.topchat.common.network

import java.lang.reflect.Type
import javax.inject.Singleton

@Singleton
interface TopchatCacheManager {
    fun saveCache(key: String, obj: Any)
    fun <T> loadCache(key: String, type: Type): T
    fun saveState(stateCacheKey: String, value: Boolean)
    fun getPreviousState(stateCacheKey: String, defaultValue: Boolean): Boolean
    fun saveLongCache(cacheKey: String, value: Long)
    fun getLongCache(cacheKey: String, defaultValue: Long): Long
}
