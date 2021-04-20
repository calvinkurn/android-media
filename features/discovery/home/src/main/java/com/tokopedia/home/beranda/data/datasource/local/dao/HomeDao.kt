package com.tokopedia.home.beranda.data.datasource.local.dao

import androidx.room.*
import com.tokopedia.home.beranda.data.datasource.local.entity.AtfCacheEntity
import com.tokopedia.home.beranda.data.model.AtfData
import com.tokopedia.home.beranda.domain.model.HomeRoomData
import kotlinx.coroutines.flow.Flow

@Dao
abstract class HomeDao{
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM HomeRoomData LIMIT 1")
    abstract fun getHomeData(): Flow<HomeRoomData?>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM AtfCacheEntity")
    abstract fun getHomeDataObject(): List<AtfCacheEntity>

    @Query("DELETE FROM AtfCacheEntity")
    abstract fun deleteAtfTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(items: List<AtfCacheEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(item: HomeRoomData)

    @Query("DELETE FROM HomeRoomData")
    abstract fun deleteHomeData()
}