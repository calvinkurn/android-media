package com.tokopedia.cacheapi.data.source.db.dao

import android.arch.persistence.room.*
import com.tokopedia.cacheapi.data.source.db.model.CacheApiVersion

@Dao
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