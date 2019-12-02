package com.tokopedia.home.beranda.data.datasource.local.dao

import androidx.room.*
import com.tokopedia.home.beranda.domain.model.HomeRoomData
import kotlinx.coroutines.flow.Flow

@Dao
abstract class HomeDao{
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM HomeRoomData LIMIT 1")
    abstract fun getHomeData(): Flow<HomeRoomData?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(item: HomeRoomData)
}