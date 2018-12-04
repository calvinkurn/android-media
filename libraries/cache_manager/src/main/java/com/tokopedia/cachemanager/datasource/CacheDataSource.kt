package com.tokopedia.cachemanager.datasource

import android.content.Context
import com.tokopedia.cachemanager.db.dao.CacheDatabaseDao
import com.tokopedia.cachemanager.db.model.CacheDbModel

abstract class CacheDataSource<U : CacheDbModel, T : CacheDatabaseDao<U>>(val context: Context) {

    abstract fun createCacheDatabaseDao(): T

    val cacheDatabaseDao: T by lazy {
        createCacheDatabaseDao()
    }

    abstract fun put(key: String, value: String, cacheDurationInMillis: Long)

    abstract fun get(key: String): U?

    fun delete(key: String) {
        cacheDatabaseDao.deleteByKey(key)
    }

    fun delete() {
        cacheDatabaseDao.deleteTable()
    }
}