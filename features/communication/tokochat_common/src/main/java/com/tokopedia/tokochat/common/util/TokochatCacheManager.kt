package com.tokopedia.tokochat.common.util

import javax.inject.Singleton

@Singleton
interface TokoChatCacheManager {
    fun saveCache(key: String, obj: Any)
    fun <T> loadCache(key: String, type: Class<T>): T?
    fun saveState(stateCacheKey: String, value: Boolean)
    fun getPreviousState(stateCacheKey: String): Boolean
}
