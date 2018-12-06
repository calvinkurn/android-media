package com.tokopedia.cachemanager.datasource

import android.content.Context
import com.tokopedia.cachemanager.db.CacheDatabase

class SaveInstanceCacheDataSource(context: Context) : CacheDataSource(context){

    override fun createMap(): HashMap<String, String?> = CacheDatabase.saveInstanceMap

    override fun put(key: String, value: String, cacheDurationInMillis: Long) {
        cacheMap.put(key, value)
    }

    override fun get(key: String): String? {
        val value = cacheMap.get(key)
        cacheMap.remove(key)
        return value
    }

}
