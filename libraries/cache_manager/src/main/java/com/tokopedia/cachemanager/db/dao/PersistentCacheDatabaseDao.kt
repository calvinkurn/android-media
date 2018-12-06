package com.tokopedia.cachemanager.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.tokopedia.cachemanager.db.model.PersistentCacheDbModel

@Dao
interface PersistentCacheDatabaseDao : CacheDatabaseDao<PersistentCacheDbModel> {

    @Query("DELETE FROM ${PersistentCacheDbModel.PERSISTENT_CACHE_TABLE_NAME}")
    override fun deleteTable()

    @Query("SELECT * FROM ${PersistentCacheDbModel.PERSISTENT_CACHE_TABLE_NAME} WHERE key LIKE :key LIMIT 1")
    override fun getCacheModel(key: String): PersistentCacheDbModel?

    @Query("DELETE FROM ${PersistentCacheDbModel.PERSISTENT_CACHE_TABLE_NAME} WHERE key = :key")
    override fun deleteByKey(key: String)

    @Query("DELETE FROM ${PersistentCacheDbModel.PERSISTENT_CACHE_TABLE_NAME} WHERE expiredTime < :timeStamp")
    override fun deleteExpiredRecords(timeStamp: Long)
}
