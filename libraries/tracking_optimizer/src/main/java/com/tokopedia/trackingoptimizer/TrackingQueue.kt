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

/***
 * This is to optimize the tracking sent to server
 *
 * Example of Input
 * trackingQueueObj.putScreenName("screen1")
 * trackingQueueObj.putEETracking(EventModel("evt1","cat1", "act1", "lbl1"), hashMapOf("ecommerce" to arrayOf("1"))
 * trackingQueueObj.putEETracking(EventModel("evt1","cat1", "act1", "lbl1"), hashMapOf("ecommerce" to arrayOf("2"))
 * trackingQueueObj.putEETracking(EventModel("evt1","cat1", "act1", "lbl1"), hashMapOf("ecommerce" to arrayOf("3"))
 * trackingQueueObj.putRegularTracking(EventModel("evt2","cat2", "act2", "lbl2"))
 * trackingQueueObj.putRegularTracking(EventModel("evt3","cat3", "act3", "lbl3"))
 * // put below onPause()
 * trackingQueueObj.sendAll()
 *
 * Example output:
 * it will send 3 hits instead of 6 hits
 * TrackingOptimizerRouter.sendEventTracking(mapOf("event" to evt1", "eventCategory" to "cat1",
 *                                                  "eventAction" to "act1", "eventLabel" to "lbl1",
 *                                                  "ecommerce" to arrayOf("1", "2", "3")),
 *                                                  "openScreen" to "screen1")
 * TrackingOptimizerRouter.sendEventTracking(mapOf("event" to evt2", "eventCategory" to "cat2",
 *                                                  "eventAction" to "act2", "eventLabel" to "lbl2"))
 * TrackingOptimizerRouter.sendEventTracking(mapOf("event" to evt3", "eventCategory" to "cat3",
 *                                                  "eventAction" to "act3", "eventLabel" to "lbl3"))
 *
 * ===================================================================================================================
 * Enhance E Commerce will be send with the limit 7000 bytes. If it is over 7000 bytes, then it will send a new row
 * For example:
 * Input:
 * trackingQueueObj.putEETracking(EventModel("evt1","cat1", "act1", "lbl1"), hashMapOf("ecommerce" to arrayOf("A...")) // 2000 bytes
 * trackingQueueObj.putEETracking(EventModel("evt1","cat1", "act1", "lbl1"), hashMapOf("ecommerce" to arrayOf("B...")) // 2000 bytes
 * trackingQueueObj.putEETracking(EventModel("evt1","cat1", "act1", "lbl1"), hashMapOf("ecommerce" to arrayOf("C...")) // 8000 bytes
 * trackingQueueObj.putEETracking(EventModel("evt1","cat1", "act1", "lbl1"), hashMapOf("ecommerce" to arrayOf("D...")) // 1000 bytes
 * Then it will send 2 hits
 * hit 1: consists of 2000Bytes A + 2000 bytes B + 3000 bytes C
 * hit 2: consists of 5000bytes C + 1000 bytes D
 */

class TrackingQueue(val context: Context) : CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = TrackingExecutors.executor + TrackingExecutors.handler

    val trackingRepository: ITrackingRepository<TrackingRegularDbModel, TrackingEEDbModel,
            TrackingEEFullDbModel, TrackingScreenNameDbModel> by lazy {
        TrackingRepository(context)
    }

    /**
     * to store screen name, auth dimension will be default.
     */
    fun putScreenName(screenName:String) {
        launch {
            trackingRepository.putScreenName(screenName)
        }
    }

    /**
     * to store screen name along with custom auth dimension (if needed)
     */
    fun putScreenName(screenName:String, screenCustomModel: ScreenCustomModel) {
        launch {
            trackingRepository.putScreenName(screenName, screenCustomModel)
        }
    }

    /**
     * to store regular tracking
     */
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
     * to store the EE tracking into db, along with custom dimension
     * Usually the map for enhanceECommerce is in form "ecommerce":<value>
     */
    fun putEETracking(event: EventModel,
                      enhanceECommerceMap: HashMap<String, Any>,
                      customDimension: HashMap<String, Any>? = null) {
        launch {
            trackingRepository.putEE(event, customDimension, enhanceECommerceMap)
        }
    }

    /**
     * to store the EE tracking into db, with no custom dimension
     * Usually the map for enhanceECommerce is in form "ecommerce":<value>
     */
    fun putEETracking(map: HashMap<String, Any>? = null,
                      enhanceECommerceMap: HashMap<String, Any>?) {
        launch {
            trackingRepository.putEE(map, enhanceECommerceMap)
        }
    }

    /**
     * send all tracking in DB to router.
     * call this function when onPause()
     */
    fun sendAll() {
        //send all tracking in db to gtm
        SendTrackQueueService.start(context)
    }

}
