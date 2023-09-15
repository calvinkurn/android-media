package com.tokopedia.home.beranda.data.datasource.local.dao

import androidx.room.*
import com.tokopedia.home.beranda.data.datasource.local.entity.AtfCacheEntity

@Dao
abstract class AtfDao {
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM AtfCacheEntity ORDER BY position ASC")
    abstract suspend fun getAtfDynamicPosition(): List<AtfCacheEntity>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM AtfCacheEntity WHERE id = :id ORDER BY position ASC")
    abstract suspend fun getAtfData(id: String): AtfCacheEntity

    @Query("DELETE FROM AtfCacheEntity")
    abstract fun deleteAtfTable()

    abstract fun saveAtf()
}
