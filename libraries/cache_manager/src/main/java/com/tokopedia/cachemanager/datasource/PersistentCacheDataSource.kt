package com.tokopedia.cachemanager.datasource

import android.content.Context
import com.tokopedia.cachemanager.db.CacheDeletion
import com.tokopedia.cachemanager.db.PersistentCacheDatabase
import com.tokopedia.cachemanager.db.dao.PersistentCacheDatabaseDao
import com.tokopedia.cachemanager.db.model.PersistentCacheDbModel
import kotlinx.coroutines.experimental.*

class PersistentCacheDataSource(context: Context) :
        CacheDataSource<PersistentCacheDbModel, PersistentCacheDatabaseDao>(context) {

    override fun createCacheDatabaseDao(): PersistentCacheDatabaseDao =
            PersistentCacheDatabase.getInstance(context).getPersistentCacheDao()

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

    override fun deleteExpired() {
        CacheDeletion.isPersistentJobActive = true
        val handler = CoroutineExceptionHandler { _, ex ->
            CacheDeletion.isPersistentJobActive = false
        }
        CoroutineScope(Dispatchers.IO + handler).launch {
            cacheDatabaseDao.deleteExpiredRecords(System.currentTimeMillis())
            CacheDeletion.setPersistentLastDelete()
            CacheDeletion.isPersistentJobActive = false
        }
    }

}
