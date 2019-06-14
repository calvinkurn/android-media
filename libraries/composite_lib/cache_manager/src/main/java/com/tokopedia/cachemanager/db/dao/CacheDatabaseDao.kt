package com.tokopedia.cachemanager.db.dao

import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Update
import com.tokopedia.cachemanager.db.model.CacheDbModel

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
    fun deleteExpiredRecords(timeStamp:Long)

}
