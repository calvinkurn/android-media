package com.tokopedia.notifcenter.util.cache

import java.lang.reflect.Type

interface NotifCenterCacheManager {
    fun saveCache(key: String, obj: Any)
    fun <T> loadCache(key: String, type: Type): T?
    fun saveCacheInt(key: String, int: Int)
    fun loadCacheInt(key: String): Int?
    fun saveCacheBoolean(key: String, bool: Boolean)
    fun loadCacheBoolean(key: String): Boolean?
}
