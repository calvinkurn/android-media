
package com.tokopedia.trackingoptimizer.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.tokopedia.trackingoptimizer.db.model.TrackingEEFullDbModel

@Dao
interface TrackingEEFullDatabaseDao : TrackingDatabaseDao<TrackingEEFullDbModel> {

    @Query("DELETE FROM ${TrackingEEFullDbModel.TRACKING_EE_FULL_TABLE_NAME}")
    override fun deleteTable()

    @Query("SELECT * FROM ${TrackingEEFullDbModel.TRACKING_EE_FULL_TABLE_NAME} WHERE id = :key LIMIT 1")
    override fun getTrackingModel(key: String): TrackingEEFullDbModel?

    @Query("SELECT * FROM ${TrackingEEFullDbModel.TRACKING_EE_FULL_TABLE_NAME}")
    override fun getTrackingModelList(): Array<TrackingEEFullDbModel>?

    @Query("DELETE FROM ${TrackingEEFullDbModel.TRACKING_EE_FULL_TABLE_NAME} WHERE id = :key")
    override fun deleteByKey(key: String)

}
