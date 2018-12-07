package com.tokopedia.cachemanager.datasource

import android.content.Context
import com.tokopedia.cachemanager.db.CacheDeletion
import com.tokopedia.cachemanager.db.SaveInstanceCacheDatabase
import com.tokopedia.cachemanager.db.dao.SaveInstanceCacheDatabaseDao
import com.tokopedia.cachemanager.db.model.SaveInstanceCacheDbModel
import kotlinx.coroutines.experimental.*

class SaveInstanceCacheDataSource(context: Context) :
        CacheDataSource<SaveInstanceCacheDbModel, SaveInstanceCacheDatabaseDao>(context) {

    override fun createCacheDatabaseDao(): SaveInstanceCacheDatabaseDao =
            SaveInstanceCacheDatabase.getInstance(context).getSaveInstanceCacheDao()

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
        val data = cacheDatabaseDao.getCacheModel(key)
        return if (data == null) {
            null
        } else {
            delete(key)
            data
        }
    }

    override fun deleteExpired() {
        CacheDeletion.isSaveInstanceJobActive = true
        val handler = CoroutineExceptionHandler { _, ex ->
            CacheDeletion.isSaveInstanceJobActive = false
        }
        CoroutineScope(Dispatchers.IO + handler).launch {
            cacheDatabaseDao.deleteExpiredRecords(System.currentTimeMillis())
            CacheDeletion.setSaveInstanceLastDelete()
            CacheDeletion.isSaveInstanceJobActive = false
        }
    }

}
