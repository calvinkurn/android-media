package com.tokopedia.cacheapi.data.source.db.dao

import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.tokopedia.cacheapi.data.source.db.model.CacheApiVersion
import com.tokopedia.cachemanager.db.model.CacheDbModel

interface CacheApiVersionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: CacheApiVersion)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(model: CacheApiVersion)

    @Query("DELETE FROM ${CacheApiVersion.TABLE_NAME}")
    fun deleteAll()

    @Query("SELECT * FROM ${CacheApiVersion.TABLE_NAME} LIMIT 1")
    fun getCurrentVersion(): CacheApiVersion?

}