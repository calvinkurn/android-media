package com.tokopedia.cachemanager.repository

import android.content.Context
import com.tokopedia.cachemanager.datasource.ICacheDataSource
import com.tokopedia.cachemanager.db.model.CacheDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class CacheRepository<U : CacheDbModel>(val context: Context) : ICacheRepository {

    abstract fun createCacheDataSource(): ICacheDataSource<U>

    abstract fun needDeleteExpired(): Boolean

    val cacheDataSource by lazy {
        createCacheDataSource()
    }

    override fun deleteExpiredRecords() {
        cacheDataSource.deleteExpired()
    }

    override fun put(key: String, value: String, cacheDurationInMillis: Long) {
        cacheDataSource.put(key, value, cacheDurationInMillis)
    }

    override fun get(key: String): String? {
        val model = cacheDataSource.get(key)
        if (needDeleteExpired()) {
            deleteExpiredRecords()
        }
        return model?.value
    }

    override fun getFlow(key: String): Flow<String?> {
        return cacheDataSource.getFlow(key).map { it?.value }
    }

    override fun delete(key: String) {
        cacheDataSource.delete(key)
    }

    override fun delete() {
        cacheDataSource.delete()
    }

}
