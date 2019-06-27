package com.tokopedia.trackingoptimizer.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.tokopedia.trackingoptimizer.db.model.TrackingEEDbModel

@Dao
interface TrackingEEDatabaseDao : TrackingDatabaseDao<TrackingEEDbModel> {

    @Query("DELETE FROM ${TrackingEEDbModel.TRACKING_EE_TABLE_NAME}")
    override fun deleteTable()

    @Query("SELECT * FROM ${TrackingEEDbModel.TRACKING_EE_TABLE_NAME} WHERE key = :key LIMIT 1")
    override fun getTrackingModel(key: String): TrackingEEDbModel?

    @Query("SELECT * FROM ${TrackingEEDbModel.TRACKING_EE_TABLE_NAME}")
    override fun getTrackingModelList(): Array<TrackingEEDbModel>?

    @Query("DELETE FROM ${TrackingEEDbModel.TRACKING_EE_TABLE_NAME} WHERE key = :key")
    override fun deleteByKey(key: String)

}
