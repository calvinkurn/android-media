package com.tokopedia.trackingoptimizer

import android.annotation.SuppressLint
import android.content.Context
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.ECOMMERCE
import com.tokopedia.trackingoptimizer.db.model.TrackingEEDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingEEFullDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingRegularDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingScreenNameDbModel
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.trackingoptimizer.repository.TrackRepository
import com.tokopedia.trackingoptimizer.repository.TrackRepositoryImpl
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

/***
 * This is to optimize the tracking sent to server
 *
 * Example of Input
 * trackingQueueObj.putEETracking(EventModel("evt1","cat1", "act1", "lbl1"), hashMapOf("ecommerce" to arrayOf("1"))
 * trackingQueueObj.putEETracking(EventModel("evt1","cat1", "act1", "lbl1"), hashMapOf("ecommerce" to arrayOf("2"))
 * trackingQueueObj.putEETracking(EventModel("evt1","cat1", "act1", "lbl1"), hashMapOf("ecommerce" to arrayOf("3"))
 * // put below onPause()
 * trackingQueueObj.sendAll()
 *
 * Example output:
 * it will send 1 hits instead of 3 hits
 * TrackingOptimizerRouter.sendEventTracking(mapOf("event" to evt1", "eventCategory" to "cat1",
 *                                                  "eventAction" to "act1", "eventLabel" to "lbl1",
 *                                                  "ecommerce" to arrayOf("1", "2", "3")),
 *                                                  "openScreen" to "screen1")
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

class TrackingQueue(ctx: Context) {

    val context = ctx.applicationContext

    companion object {
        @SuppressLint("StaticFieldLeak")
        internal var trackingQueueObj: TrackingQueueSingleton? = null
    }

    private fun getTrackingQueueObject(ctx: Context): TrackingQueueSingleton {
        var obj = trackingQueueObj
        return if (obj == null) {
            obj = TrackingQueueSingleton(ctx)
            trackingQueueObj = obj
            obj
        } else {
            obj
        }
    }

    /**
     * to store the tracking into db
     * if contains "ecommerce" key, it will considered as enhance e commerce tracking
     * The other keys will be considered as custom dimensions
     */
    fun putEETracking(map: HashMap<String, Any>? = null) {
        getTrackingQueueObject(context).putEETracking(map)
    }

    /**
     * to store the EE tracking into db, along with custom dimension
     * Usually the map for enhanceECommerce is in form "ecommerce":<value>
     */
    fun putEETracking(
        event: EventModel,
        enhanceECommerceMap: HashMap<String, Any>,
        customDimension: HashMap<String, Any>? = null
    ) {
        getTrackingQueueObject(context).putEETracking(event, enhanceECommerceMap, customDimension)
    }

    /**
     * to store the EE tracking into db, with no custom dimension
     * Usually the map for enhanceECommerce is in form "ecommerce":<value>
     */
    fun putEETracking(
        map: HashMap<String, Any>? = null,
        enhanceECommerceMap: HashMap<String, Any>?
    ) {
        getTrackingQueueObject(context).putEETracking(map, enhanceECommerceMap)
    }

    /**
     * send all tracking in DB to router.
     * no longer need to call this function when onPause()
     * because this method is called within an ActivityLifeCycleCallbacks
     * validation is added, to start the service only if tracking queue is available
     */
    @Deprecated("No need to call this manually.")
    fun sendAll() {
        getTrackingQueueObject(context).sendAll()
    }
}

internal class TrackingQueueSingleton(ctx: Context) : CoroutineScope {

    val context = ctx.applicationContext

    companion object {
        var mightHaveData = true
    }

    override val coroutineContext: CoroutineContext
        get() = TrackingExecutors.executor + TrackingExecutors.handler

    val newTrackingRepository: TrackRepositoryImpl<TrackingRegularDbModel, TrackingEEDbModel,
            TrackingEEFullDbModel, TrackingScreenNameDbModel> by lazy {
        TrackRepository(context)
    }

    internal fun putEETracking(map: HashMap<String, Any>? = null) {
        if (map?.containsKey(ECOMMERCE) != true) {
            return
        }
        launch {
            try {
                newTrackingRepository.put(map)
                mightHaveData = true
            } catch (ignored: Throwable) {
            }
        }
    }

    internal fun putEETracking(
        event: EventModel,
        enhanceECommerceMap: HashMap<String, Any>,
        customDimension: HashMap<String, Any>? = null
    ) {
        launch {
            try {
                newTrackingRepository.putEE(event, customDimension, enhanceECommerceMap)
                mightHaveData = true
            } catch (ignored: Throwable) {
            }
        }
    }

    internal fun putEETracking(
        map: HashMap<String, Any>? = null,
        enhanceECommerceMap: HashMap<String, Any>?
    ) {
        launch {
            try {
                newTrackingRepository.putEE(map, enhanceECommerceMap)
                mightHaveData = true
            } catch (ignored: Throwable) {
            }
        }
    }

    private fun isTrackingQueueAvailable(): Boolean {
        return newTrackingRepository.hasDataToSend()
    }

    internal fun sendAll() {
        // send all tracking in db to gtm
        // launch in different coroutine scope.
        launch(Dispatchers.Default) {
            try {
                // atomicInteger to allow only 1 service at a time
                // isTrackingQueueAvailable is to check if the db has any data to send
                if (atomicInteger.get() < 1 && mightHaveData) {
                    SendTrackQueueService.start(context)
                }
            } catch (ignored: Throwable) {
            }
        }
    }
}
