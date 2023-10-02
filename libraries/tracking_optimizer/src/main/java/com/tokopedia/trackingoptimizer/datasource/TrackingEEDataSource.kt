package com.tokopedia.trackingoptimizer.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.tokopedia.trackingoptimizer.db.TrackingDatabase
import com.tokopedia.trackingoptimizer.db.dao.TrackingEEDatabaseDao
import com.tokopedia.trackingoptimizer.db.model.TrackingEEDbModel
import com.tokopedia.trackingoptimizer.gson.GsonSingleton
import com.tokopedia.trackingoptimizer.gson.HashMapJsonUtil
import com.tokopedia.trackingoptimizer.model.EventModel

class TrackingEEDataSource private constructor(context: Context) :
    TrackingDataSource<TrackingEEDbModel, TrackingEEDatabaseDao>(context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: TrackingEEDataSource? = null

        @JvmStatic
        fun getInstance(context: Context): TrackingEEDataSource {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TrackingEEDataSource(context.applicationContext)
            }
        }
    }

    override fun createTrackingDatabaseDao(): TrackingEEDatabaseDao =
        TrackingDatabase.getInstance(context).getTrackingEEDao()

    override fun put(
        event: EventModel, customDimension: HashMap<String, Any>?,
        enhanceECommerceMap: HashMap<String, Any>?
    ) {
        trackingDatabaseDao.insertSingle(
            TrackingEEDbModel().apply {
                this.key = event.key
                this.event = GsonSingleton.instance.toJson(event)
                this.customDimension = HashMapJsonUtil.mapToJson(customDimension).toString()
                this.enhanceEcommerce = HashMapJsonUtil.mapToJson(enhanceECommerceMap).toString()
            }
        )
    }

    override fun put(model: TrackingEEDbModel) {
        trackingDatabaseDao.insertSingle(model)
    }

}
