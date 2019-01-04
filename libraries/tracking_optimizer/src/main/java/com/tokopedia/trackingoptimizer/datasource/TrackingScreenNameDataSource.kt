package com.tokopedia.trackingoptimizer.datasource

import android.content.Context
import com.tokopedia.trackingoptimizer.db.TrackingDatabase
import com.tokopedia.trackingoptimizer.db.dao.TrackingScreenNameDatabaseDao
import com.tokopedia.trackingoptimizer.db.model.TrackingScreenNameDbModel
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.trackingoptimizer.model.ScreenCustomModel

class TrackingScreenNameDataSource(val context: Context) {

    val trackingDatabaseDao: TrackingScreenNameDatabaseDao by lazy {
        createTrackingDatabaseDao()
    }

    fun delete(event: EventModel) {
        trackingDatabaseDao.deleteByKey(event.key)
    }

    fun delete() {
        trackingDatabaseDao.deleteTable()
    }

    fun get(screenName: String): TrackingScreenNameDbModel? {
        return trackingDatabaseDao.getTrackingModel(screenName)
    }

    fun getAll(): Array<TrackingScreenNameDbModel>? {
        return trackingDatabaseDao.getTrackingModelList()
    }

    fun createTrackingDatabaseDao(): TrackingScreenNameDatabaseDao =
            TrackingDatabase.getInstance(context).getTrackingScreenNameDao()

    fun put(screenName: String) {
        trackingDatabaseDao.insertSingle(
                TrackingScreenNameDbModel().apply {
                    this.screenName = screenName
                }
        )
    }

    fun put(screenName: String, customModel: ScreenCustomModel) {
        trackingDatabaseDao.insertSingle(
                TrackingScreenNameDbModel().apply {
                    this.screenName = screenName
                    this.shopId = customModel.shopId
                    this.pageType = customModel.pageType
                    this.shopType = customModel.shopType
                    this.productId = customModel.productId
                }
        )
    }

    fun put(model: TrackingScreenNameDbModel) {
        trackingDatabaseDao.insertSingle(model)
    }

}
