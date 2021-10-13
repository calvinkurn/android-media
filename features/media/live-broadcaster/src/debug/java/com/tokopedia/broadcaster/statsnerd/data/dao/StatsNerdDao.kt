package com.tokopedia.broadcaster.statsnerd.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tokopedia.broadcaster.statsnerd.data.entity.StatsNerdLog

@Dao
interface StatsNerdDao {

    @Query("SELECT * FROM ChuckerLog")
    fun chuckers(): List<StatsNerdLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun logChucker(log: StatsNerdLog)

    @Query("DELETE FROM ChuckerLog")
    fun delete()

}