package com.tokopedia.trackingoptimizer

import android.content.Context
import com.tokopedia.trackingoptimizer.db.model.TrackingEEDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingEEFullDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingRegularDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingScreenNameDbModel
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.trackingoptimizer.model.ScreenCustomModel
import com.tokopedia.trackingoptimizer.repository.ITrackingRepository
import com.tokopedia.trackingoptimizer.repository.TrackingRepository
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

class TrackingQueue(val context: Context) : CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = TrackingExecutors.executor + TrackingExecutors.handler

    val trackingRepository: ITrackingRepository<TrackingRegularDbModel, TrackingEEDbModel,
            TrackingEEFullDbModel, TrackingScreenNameDbModel> by lazy {
        TrackingRepository(context)
    }

    fun putScreenName(screenName:String) {
        launch {
            trackingRepository.putScreenName(screenName)
        }
    }

    fun putScreenName(screenName:String, screenCustomModel: ScreenCustomModel) {
        launch {
            trackingRepository.putScreenName(screenName, screenCustomModel)
        }
    }

    fun putRegularTracking(event: EventModel, customDimension: HashMap<String, Any>? = null) {
        launch {
            trackingRepository.putRegular(event, customDimension)
        }
    }

    /**
     * to store the tracking into db
     * map should contain "event", "eventCategory", "eventAction", and "eventAction"
     * If there is no those key, it will do nothing.
     * if there contains "ecommerce" key, it will considered as enhance e commerce tracking
     * The other keys will be considered as custom dimensions
     */
    fun putTracking(map: HashMap<String, Any>? = null) {
        launch {
            trackingRepository.put(map)
        }
    }

    /**
     * to store the EE tracking into db
     */
    fun putEETracking(event: EventModel,
                      enhanceECommerceMap: HashMap<String, Any>,
                      customDimension: HashMap<String, Any>? = null) {
        launch {
            trackingRepository.putEE(event, customDimension, enhanceECommerceMap)
        }
    }

    fun putEETracking(map: HashMap<String, Any>? = null,
                      enhanceECommerceMap: HashMap<String, Any>?) {
        launch {
            trackingRepository.putEE(map, enhanceECommerceMap)
        }
    }

    fun sendAll() {
        //send all tracking in db to gtm
        SendTrackQueueService.start(context)
    }

}
