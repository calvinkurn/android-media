package com.tokopedia.broadcaster.statsnerd.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tokopedia.broadcaster.statsnerd.data.entity.ChuckerLog

@Dao
interface ChuckerDao {

    @Query("SELECT * FROM ChuckerLog")
    fun chuckers(): List<ChuckerLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun logChucker(log: ChuckerLog)

    @Query("DELETE FROM ChuckerLog")
    fun delete()

}