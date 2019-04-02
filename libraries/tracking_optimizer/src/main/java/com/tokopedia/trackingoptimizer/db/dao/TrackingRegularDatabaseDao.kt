package com.tokopedia.trackingoptimizer.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.tokopedia.trackingoptimizer.db.model.TrackingEEDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingRegularDbModel

@Dao
interface TrackingRegularDatabaseDao : TrackingDatabaseDao<TrackingRegularDbModel> {

    @Query("DELETE FROM ${TrackingRegularDbModel.TRACKING_REGULAR_TABLE_NAME} WHERE id = :key")
    override fun deleteByKey(key: String)

    @Query("DELETE FROM ${TrackingRegularDbModel.TRACKING_REGULAR_TABLE_NAME}")
    override fun deleteTable()

    @Query("SELECT * FROM ${TrackingRegularDbModel.TRACKING_REGULAR_TABLE_NAME} WHERE id = :key LIMIT 1")
    override fun getTrackingModel(key: String): TrackingRegularDbModel?

    @Query("SELECT * FROM ${TrackingRegularDbModel.TRACKING_REGULAR_TABLE_NAME}")
    override fun getTrackingModelList(): Array<TrackingRegularDbModel>?

}