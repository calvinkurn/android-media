package com.tokopedia.trackingoptimizer.db.dao

import android.arch.persistence.room.*
import com.tokopedia.trackingoptimizer.db.model.TrackingScreenNameDbModel

@Dao
interface TrackingScreenNameDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingle(model: TrackingScreenNameDbModel)

    @Query("DELETE FROM ${TrackingScreenNameDbModel.TRACKING_SCREEN_NAME_TABLE_NAME} WHERE screenName = :key")
    fun deleteByKey(key: String)

    @Query("DELETE FROM ${TrackingScreenNameDbModel.TRACKING_SCREEN_NAME_TABLE_NAME}")
    fun deleteTable()

    @Query("SELECT * FROM ${TrackingScreenNameDbModel.TRACKING_SCREEN_NAME_TABLE_NAME} WHERE screenName = :key LIMIT 1")
    fun getTrackingModel(key: String): TrackingScreenNameDbModel?

    @Query("SELECT * FROM ${TrackingScreenNameDbModel.TRACKING_SCREEN_NAME_TABLE_NAME}")
    fun getTrackingModelList(): Array<TrackingScreenNameDbModel>?

}