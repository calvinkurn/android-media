package com.tokopedia.home.beranda.data.datasource.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tokopedia.home.beranda.domain.model.HomeRoomData

@Dao
abstract class HomeDao{
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM HomeRoomData LIMIT 1")
    abstract fun getHomeData(): LiveData<HomeRoomData?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(item: HomeRoomData)

    @Query("DELETE FROM HomeRoomData")
    abstract fun deleteHomeData()
}