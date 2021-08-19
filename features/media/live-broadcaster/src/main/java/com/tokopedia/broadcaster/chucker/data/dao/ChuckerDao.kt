package com.tokopedia.broadcaster.chucker.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tokopedia.broadcaster.chucker.data.entity.ChuckerLog

@Dao
interface ChuckerDao {

    @Query("SELECT * FROM ChuckerLog")
    fun chuckers(): LiveData<List<ChuckerLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun logChucker(log: ChuckerLog)

    @Query("DELETE FROM ChuckerLog")
    fun delete()

}