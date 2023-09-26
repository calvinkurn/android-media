package com.tokopedia.home.beranda.data.datasource.local.dao

import androidx.room.*
import com.tokopedia.home.beranda.data.datasource.local.entity.AtfCacheEntity

@Dao
abstract class AtfDao {
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM AtfCacheEntity ORDER BY position ASC")
    abstract suspend fun getAtfDynamicPosition(): List<AtfCacheEntity>

    @Query("DELETE FROM AtfCacheEntity")
    abstract suspend fun deleteAtfTable()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun updateAtfData(atfCacheEntity: AtfCacheEntity)
}
