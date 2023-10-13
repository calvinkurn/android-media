package com.tokopedia.iris.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tokopedia.iris.data.db.table.PerformanceTracking
import com.tokopedia.iris.data.db.table.Tracking

/**
 * @author okasurya on 10/18/18.
 */
@Dao
interface TrackingDao {
    @Query("SELECT * FROM tracking ORDER BY timeStamp, userId ASC LIMIT :limit")
    fun getFromOldest(limit: Int): List<Tracking>

    @Query("SELECT COUNT(timeStamp) FROM tracking")
    fun getCount(): Int

    @Delete
    fun delete(trackingList: List<Tracking>): Int

    @Insert
    fun insert(tracking: Tracking): Long

    @Query("DELETE FROM tracking")
    fun flush()
}

@Dao
interface TrackingPerfDao {
    @Query("SELECT * FROM tracking_perf ORDER BY timeStamp, userId ASC LIMIT :limit")
    fun getFromOldest(limit: Int): List<PerformanceTracking>

    @Query("SELECT COUNT(timeStamp) FROM tracking_perf")
    fun getCount(): Int

    @Delete
    fun delete(trackingList: List<PerformanceTracking>): Int

    @Insert
    fun insert(tracking: PerformanceTracking): Long

    @Query("DELETE FROM tracking_perf")
    fun flush()
}