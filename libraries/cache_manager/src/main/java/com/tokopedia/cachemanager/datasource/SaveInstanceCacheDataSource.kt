package com.tokopedia.cachemanager.datasource

import android.content.Context
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.cachemanager.db.CacheDatabase
import com.tokopedia.cachemanager.db.dao.SaveInstanceCacheDatabaseDao
import com.tokopedia.cachemanager.db.model.SaveInstanceCacheDbModel

class SaveInstanceCacheDataSource(context: Context) :
        CacheDataSource<SaveInstanceCacheDbModel, SaveInstanceCacheDatabaseDao>(context) {

    override fun createCacheDatabaseDao(): SaveInstanceCacheDatabaseDao =
            CacheDatabase.getInstance(context).getSaveInstanceCacheDao()

    override fun put(key: String, value: String, cacheDurationInMillis: Long) {
        cacheDatabaseDao.insertCacheSingle(
                SaveInstanceCacheDbModel().apply {
                    this.key = key
                    this.value = value
                    this.expiredTime = System.currentTimeMillis() + cacheDurationInMillis;
                }
        )
    }

    override fun get(key: String): SaveInstanceCacheDbModel? {
        CacheManager.deleteSaveInstanceExpiration(context);
        val data = cacheDatabaseDao.getCacheModel(key)
        return if (data == null) {
            null
        } else {
            delete(key)
            data
        }
    }

}
