package com.tokopedia.trackingoptimizer.datasource

import android.content.Context
import android.util.Log
import com.tokopedia.trackingoptimizer.db.TrackingDatabase
import com.tokopedia.trackingoptimizer.db.dao.TrackingRegularDatabaseDao
import com.tokopedia.trackingoptimizer.db.model.TrackingRegularDbModel
import com.tokopedia.trackingoptimizer.gson.GsonSingleton
import com.tokopedia.trackingoptimizer.gson.HashMapJsonUtil
import com.tokopedia.trackingoptimizer.model.EventModel

class TrackingRegularDataSource(context: Context) :
        TrackingDataSource<TrackingRegularDbModel, TrackingRegularDatabaseDao>(context) {

    override fun createTrackingDatabaseDao(): TrackingRegularDatabaseDao =
            TrackingDatabase.getInstance(context).getTrackingRegularDao()

    override fun put(event: EventModel, customDimension: HashMap<String, Any>?,
                     enhanceECommerceMap: HashMap<String, Any>?) {
        trackingDatabaseDao.insertSingle(
                TrackingRegularDbModel().apply {
                    this.event = GsonSingleton.instance.toJson(event)
                    this.customDimension = HashMapJsonUtil.mapToJson(customDimension).toString()
                }
        )
    }

    override fun put(model: TrackingRegularDbModel) {
        trackingDatabaseDao.insertSingle(model)
    }

}