package com.tokopedia.cachemanager.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.tokopedia.cachemanager.db.model.CacheDbModel
import kotlinx.coroutines.flow.Flow

interface CacheDatabaseDao<T : CacheDbModel> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCacheSingle(model: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultiple(models: List<T>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(model: T)

    fun deleteByKey(key: String)
    fun deleteTable()
    fun getCacheModel(key: String): T?
    fun getCacheModelFlow(key: String): Flow<T?>
    fun deleteExpiredRecords(timeStamp:Long)

}
