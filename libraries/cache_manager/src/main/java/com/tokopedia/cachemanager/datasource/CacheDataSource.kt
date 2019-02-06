package com.tokopedia.cachemanager.datasource

import android.content.Context
import com.tokopedia.cachemanager.db.dao.CacheDatabaseDao
import com.tokopedia.cachemanager.db.model.CacheDbModel

abstract class CacheDataSource<U : CacheDbModel, T : CacheDatabaseDao<U>>(val context: Context)
    :ICacheDataSource<U> {

    abstract fun createCacheDatabaseDao(): T

    val cacheDatabaseDao: T by lazy {
        createCacheDatabaseDao()
    }

    override fun delete(key: String) {
        cacheDatabaseDao.deleteByKey(key)
    }

    override fun delete() {
        cacheDatabaseDao.deleteTable()
    }
}
