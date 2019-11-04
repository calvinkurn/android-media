
package com.tokopedia.trackingoptimizer.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.tokopedia.trackingoptimizer.db.model.TrackingEEDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingEEFullDbModel

@Dao
interface TrackingEEFullDatabaseDao : TrackingDatabaseDao<TrackingEEFullDbModel> {

    @Query("DELETE FROM ${TrackingEEFullDbModel.TRACKING_EE_FULL_TABLE_NAME}")
    override fun deleteTable()

    @Query("SELECT * FROM ${TrackingEEFullDbModel.TRACKING_EE_FULL_TABLE_NAME} WHERE id = :key LIMIT 1")
    override fun getTrackingModel(key: String): TrackingEEFullDbModel?

    @Query("SELECT * FROM ${TrackingEEFullDbModel.TRACKING_EE_FULL_TABLE_NAME}")
    override fun getTrackingModelList(): Array<TrackingEEFullDbModel>?

    @Query("SELECT * FROM ${TrackingEEFullDbModel.TRACKING_EE_FULL_TABLE_NAME} LIMIT :limit")
    override fun getTrackingModelList(limit: Int): Array<TrackingEEFullDbModel>?

    @Query("DELETE FROM ${TrackingEEFullDbModel.TRACKING_EE_FULL_TABLE_NAME} WHERE id = :key")
    override fun deleteByKey(key: String)

    @Query("DELETE FROM ${TrackingEEFullDbModel.TRACKING_EE_FULL_TABLE_NAME} WHERE id in (:keyList)")
    override fun deleteByKeyList(keyList: List<String>)

}
