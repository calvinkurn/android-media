package com.tokopedia.cacheapi.data.source.db.dao

import android.arch.persistence.room.*
import com.tokopedia.cacheapi.data.source.db.model.CacheApiData

@Dao
interface CacheApiDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: CacheApiData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(model: CacheApiData)

    @Query("DELETE FROM ${CacheApiData.TABLE_NAME}")
    fun deleteAll()

    @Query("DELETE FROM ${CacheApiData.TABLE_NAME} where ${CacheApiData.COLUMN_EXPIRED_TIME} < :currentTime")
    fun deleteOldData(currentTime: Long)

    @Query("DELETE FROM ${CacheApiData.TABLE_NAME} where ${CacheApiData.COLUMN_WHITE_LIST_ID} = :whiteListId")
    fun deleteWhiteList(whiteListId: Long)

    @Query("SELECT * FROM ${CacheApiData.TABLE_NAME} WHERE ${CacheApiData.COLUMN_HOST} = :host and ${CacheApiData.COLUMN_PATH} = :path and ${CacheApiData.COLUMN_REQUEST_PARAM} = :requestParam LIMIT 1")
    fun getData(host: String, path: String, requestParam: String): CacheApiData?

}