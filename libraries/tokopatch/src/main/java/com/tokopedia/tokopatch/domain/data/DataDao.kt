package com.tokopedia.tokopatch.domain.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Author errysuprayogi on 09,June,2020
 */

@Dao
interface DataDao {

    @Query("SELECT * FROM result WHERE version_code LIKE :versionCode")
    fun getAllResultList(versionCode: String): List<DataResponse.Result>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(result: DataResponse.Result)

    @Delete
    suspend fun delete(result: DataResponse.Result)

    @Query("DELETE FROM result")
    suspend fun deleteAll()

    @Query("SELECT * FROM status WHERE id = :deviceId")
    suspend fun getStatus(deviceId: String): DataStatus

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setStatus(status: DataStatus)
}