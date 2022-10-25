package com.tokopedia.tokochat_common.util

import java.lang.reflect.Type
import javax.inject.Singleton

@Singleton
interface TokoChatCacheManager {
    fun saveCache(key: String, obj: Any)
    fun <T> loadCache(key: String, type: Type): T?
    fun saveState(stateCacheKey: String, value: Boolean)
    fun getPreviousState(stateCacheKey: String): Boolean
}
