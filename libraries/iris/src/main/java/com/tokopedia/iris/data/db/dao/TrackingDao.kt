package com.tokopedia.iris.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.tokopedia.iris.data.db.table.Tracking

/**
 * @author okasurya on 10/18/18.
 */
@Dao
interface TrackingDao {
    @Query("SELECT * FROM tracking ORDER BY timeStamp, userId ASC LIMIT :limit")
    fun getFromOldest(limit: Int): List<Tracking>

    @Delete
    fun delete(dataTracking: List<Tracking>)

    @Insert
    fun insert(tracking: Tracking): Long

    @Query("DELETE FROM tracking")
    fun flush()
}