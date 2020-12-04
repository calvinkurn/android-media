package com.tokopedia.inbox.domain.cache

import java.lang.reflect.Type

interface InboxCacheManager {
    fun saveCache(key: String, obj: Any)
    fun saveCacheInt(key: String, int: Int)
    fun <T> loadCache(key: String, type: Type): T?
    fun loadCacheInt(key: String): Int?
}