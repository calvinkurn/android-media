
package com.tokopedia.trackingoptimizer.datasource

import android.content.Context
import android.util.Log
import com.tokopedia.trackingoptimizer.db.TrackingDatabase
import com.tokopedia.trackingoptimizer.db.dao.TrackingEEFullDatabaseDao
import com.tokopedia.trackingoptimizer.db.model.TrackingEEFullDbModel
import com.tokopedia.trackingoptimizer.gson.GsonSingleton
import com.tokopedia.trackingoptimizer.gson.HashMapJsonUtil
import com.tokopedia.trackingoptimizer.model.EventModel

class TrackingEEFullDataSource(context: Context) :
        TrackingDataSource<TrackingEEFullDbModel, TrackingEEFullDatabaseDao>(context) {

    override fun createTrackingDatabaseDao(): TrackingEEFullDatabaseDao =
            TrackingDatabase.getInstance(context).getTrackingEEFullDao()

    override fun put(event: EventModel, customDimension: HashMap<String, Any>?,
                     enhanceECommerceMap: HashMap<String, Any>?) {
        trackingDatabaseDao.insertSingle(
                TrackingEEFullDbModel().apply {
                    this.event = GsonSingleton.instance.toJson(event)
                    this.customDimension = HashMapJsonUtil.mapToJson(customDimension).toString()
                    this.enhanceEcommerce = HashMapJsonUtil.mapToJson(enhanceECommerceMap).toString()
                }
        )
    }

    fun put(event: String, customDimension: String,
            enhanceECommerceMapString: String) {
        trackingDatabaseDao.insertSingle(
                TrackingEEFullDbModel().apply {
                    this.event = event
                    this.customDimension = customDimension
                    this.enhanceEcommerce = enhanceECommerceMapString
                }
        )
    }

    fun put(event: String, customDimension: String,
            enhanceECommerceMap: HashMap<String, Any>?) {
        trackingDatabaseDao.insertSingle(
                TrackingEEFullDbModel().apply {
                    this.event = event
                    this.customDimension = customDimension
                    this.enhanceEcommerce = HashMapJsonUtil.mapToJson(enhanceECommerceMap).toString()
                }
        )
    }

    override fun put(model: TrackingEEFullDbModel) {
        trackingDatabaseDao.insertSingle(model)
    }

}
