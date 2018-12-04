package com.tokopedia.cachemanager.datasource

import android.content.Context
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.cachemanager.db.CacheDatabase
import com.tokopedia.cachemanager.db.dao.PersistentCacheDatabaseDao
import com.tokopedia.cachemanager.db.model.PersistentCacheDbModel

class PersistentCacheDataSource(context: Context) :
        CacheDataSource<PersistentCacheDbModel, PersistentCacheDatabaseDao>(context) {

    override fun createCacheDatabaseDao(): PersistentCacheDatabaseDao =
            CacheDatabase.getInstance(context).getPersistentCacheDao()

    override fun put(key: String, value: String, cacheDurationInMillis: Long) {
        cacheDatabaseDao.insertCacheSingle(
                PersistentCacheDbModel().apply {
                    this.key = key
                    this.value = value
                    this.expiredTime = System.currentTimeMillis() + cacheDurationInMillis;
                }
        )
    }

    override fun get(key: String): PersistentCacheDbModel? {
        CacheManager.deletePersistentExpiration(context);
        val data = cacheDatabaseDao.getCacheModel(key)
        return if (data == null) {
            null
        } else if (data.expiredTime < System.currentTimeMillis()) {
            delete(key)
            null
        } else {
            data
        }
    }

}
