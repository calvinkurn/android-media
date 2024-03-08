
package com.tokopedia.cachemanager.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.tokopedia.cachemanager.db.model.SaveInstanceCacheDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SaveInstanceCacheDatabaseDao : CacheDatabaseDao<SaveInstanceCacheDbModel>{

    @Query("DELETE FROM ${SaveInstanceCacheDbModel.SAVE_INSTANCE_CACHE_TABLE_NAME} WHERE key = :key")
    override fun deleteByKey(key: String)

    @Query("DELETE FROM ${SaveInstanceCacheDbModel.SAVE_INSTANCE_CACHE_TABLE_NAME}")
    override fun deleteTable()

    @Query("SELECT * FROM ${SaveInstanceCacheDbModel.SAVE_INSTANCE_CACHE_TABLE_NAME} WHERE key LIKE :key LIMIT 1")
    override fun getCacheModel(key: String): SaveInstanceCacheDbModel?

    @Query("SELECT * FROM ${SaveInstanceCacheDbModel.SAVE_INSTANCE_CACHE_TABLE_NAME} WHERE key LIKE :key LIMIT 1")
    override fun getCacheModelFlow(key: String): Flow<SaveInstanceCacheDbModel?>

    @Query("DELETE FROM ${SaveInstanceCacheDbModel.SAVE_INSTANCE_CACHE_TABLE_NAME} WHERE expiredTime < :timeStamp")
    override fun deleteExpiredRecords(timeStamp: Long)
}
