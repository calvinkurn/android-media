package com.tokopedia.cacheapi.data.source.db.dao

import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.tokopedia.cacheapi.data.source.db.model.CacheApiWhitelist

interface CacheApiWhitelistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: CacheApiWhitelist)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(model: CacheApiWhitelist)

    @Query("DELETE FROM ${CacheApiWhitelist.TABLE_NAME}")
    fun deleteAll()

    @Query("SELECT * FROM ${CacheApiWhitelist.TABLE_NAME} WHERE ${CacheApiWhitelist.COLUMN_HOST} = :host and ${CacheApiWhitelist.COLUMN_PATH} = :path LIMIT 1")
    fun getData(host: String, path: String): CacheApiWhitelist?

    @Query("SELECT * FROM ${CacheApiWhitelist.TABLE_NAME} WHERE ${CacheApiWhitelist.COLUMN_HOST} = :host and ${CacheApiWhitelist.COLUMN_DYNAMIC_LINK} = :dynamicLink LIMIT 1")
    fun getHostList(dynamicLink: Boolean, host: String): List<CacheApiWhitelist>

}