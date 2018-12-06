package com.tokopedia.cachemanager.datasource

import android.content.Context
import com.tokopedia.cachemanager.db.CacheDatabase

class PersistentCacheDataSource(context: Context) :
        CacheDataSource(context) {

    override fun createMap(): HashMap<String, String?> = CacheDatabase.persistentMap

    override fun put(key: String, value: String, cacheDurationInMillis: Long) {
        cacheMap.put(key, value)
    }

    override fun get(key: String): String? {
        return cacheMap.get(key)
    }
}
