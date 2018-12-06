package com.tokopedia.cachemanager.repository

import android.content.Context
import com.tokopedia.cachemanager.datasource.ICacheDataSource

abstract class CacheRepository(val context: Context) : ICacheRepository {

    abstract fun createCacheDataSource(): ICacheDataSource

    val cacheDataSource by lazy {
        createCacheDataSource()
    }


    override fun put(key: String, value: String, cacheDurationInMillis: Long) {
        cacheDataSource.put(key, value, cacheDurationInMillis)
    }

    override fun get(key: String): String? {
        return cacheDataSource.get(key)
    }

}