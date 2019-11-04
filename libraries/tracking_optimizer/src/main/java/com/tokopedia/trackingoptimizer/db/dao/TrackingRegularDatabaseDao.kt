package com.tokopedia.trackingoptimizer.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.tokopedia.trackingoptimizer.db.model.TrackingEEDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingEEFullDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingRegularDbModel

@Dao
interface TrackingRegularDatabaseDao : TrackingDatabaseDao<TrackingRegularDbModel> {

    @Query("DELETE FROM ${TrackingRegularDbModel.TRACKING_REGULAR_TABLE_NAME} WHERE id = :key")
    override fun deleteByKey(key: String)

    @Query("DELETE FROM ${TrackingRegularDbModel.TRACKING_REGULAR_TABLE_NAME}")
    override fun deleteTable()

    @Query("DELETE FROM ${TrackingRegularDbModel.TRACKING_REGULAR_TABLE_NAME} WHERE id in (:keyList)")
    override fun deleteByKeyList(keyList: List<String>)

    @Query("SELECT * FROM ${TrackingRegularDbModel.TRACKING_REGULAR_TABLE_NAME} WHERE id = :key LIMIT 1")
    override fun getTrackingModel(key: String): TrackingRegularDbModel?

    @Query("SELECT * FROM ${TrackingRegularDbModel.TRACKING_REGULAR_TABLE_NAME}")
    override fun getTrackingModelList(): Array<TrackingRegularDbModel>?

    @Query("SELECT * FROM ${TrackingRegularDbModel.TRACKING_REGULAR_TABLE_NAME} LIMIT :limit")
    override fun getTrackingModelList(limit: Int): Array<TrackingRegularDbModel>?

}