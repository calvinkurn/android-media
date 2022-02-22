package com.tokopedia.broadcaster.log.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tokopedia.broadcaster.log.data.entity.NetworkLog

@Dao
interface NetworkLogDao {

    @Query("SELECT * FROM ChuckerLog")
    fun chuckers(): List<NetworkLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun logChucker(log: NetworkLog)

    @Query("DELETE FROM ChuckerLog")
    fun delete()

}