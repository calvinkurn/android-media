package com.tokopedia.trackingoptimizer.datasource

import android.content.Context
import com.tokopedia.trackingoptimizer.db.dao.TrackingDatabaseDao
import com.tokopedia.trackingoptimizer.db.model.TrackingDbModel
import com.tokopedia.trackingoptimizer.model.EventModel

abstract class TrackingDataSource<U : TrackingDbModel, T : TrackingDatabaseDao<U>>(val context: Context)
    : ITrackingDataSource<U> {

    abstract fun createTrackingDatabaseDao(): T

    val trackingDatabaseDao: T by lazy {
        createTrackingDatabaseDao()
    }

    override fun delete(event: EventModel) {
        trackingDatabaseDao.deleteByKey(event.key)
    }

    override fun delete(keyList: List<String>) {
        trackingDatabaseDao.deleteByKeyList(keyList)
    }

    override fun delete() {
        trackingDatabaseDao.deleteTable()
    }

    override fun get(event: EventModel): U? {
        return trackingDatabaseDao.getTrackingModel(event.key)
    }

    override fun getAll(): Array<U>? {
        return trackingDatabaseDao.getTrackingModelList()
    }

    override fun get(limit:Int): Array<U>? {
        return trackingDatabaseDao.getTrackingModelList(limit)
    }
}
