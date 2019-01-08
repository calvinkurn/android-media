package com.tokopedia.trackingoptimizer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.google.gson.reflect.TypeToken
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.EVENT
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.EVENT_ACTION
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.EVENT_CATEGORY
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.EVENT_LABEL
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.OPEN_SCREEN
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.SCREEN_NAME
import com.tokopedia.trackingoptimizer.db.model.*
import com.tokopedia.trackingoptimizer.gson.GsonSingleton
import com.tokopedia.trackingoptimizer.gson.HashMapJsonUtil
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.trackingoptimizer.model.ScreenCustomModel
import com.tokopedia.trackingoptimizer.repository.ITrackingRepository
import com.tokopedia.trackingoptimizer.repository.TrackingRepository
import kotlinx.coroutines.experimental.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Created by hendry on 27/12/18.
 */
class SendTrackQueueService : Service(), CoroutineScope {

    val trackingRepository: ITrackingRepository<TrackingRegularDbModel, TrackingEEDbModel,
            TrackingEEFullDbModel, TrackingScreenNameDbModel> by lazy {
        TrackingRepository(this)
    }

    val trackingOptimizerRouter: TrackingOptimizerRouter? by lazy {
        val application = this.application
        if (application is TrackingOptimizerRouter) {
            application
        } else {
            null
        }
    }

    var screenName: String? = null
    val handler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, ex ->
            decreaseCounter()
        }
    }

    companion object {
        var atomicInteger = AtomicInteger()
        val doMergeScreenName = true
        fun start(context: Context) {
            // allowing only 1 service at a time
            if (atomicInteger.get() < 1) {
                context.startService(Intent(context, SendTrackQueueService::class.java))
            }
        }
    }

    // allowing only 1 thread at a time
    override val coroutineContext: CoroutineContext by lazy {
        TrackingExecutors.executor + handler
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        atomicInteger.getAndIncrement()
        launch {
            val screenNameDbModelList = trackingRepository.getAllScreenName()

            var deleteScreenNameJob: Job? = null
            if (screenNameDbModelList != null && screenNameDbModelList.isNotEmpty()) {
                val screenNameDbModel = screenNameDbModelList[0]
                screenName = screenNameDbModel.screenName
                val customModel = ScreenCustomModel(screenNameDbModel.shopId,
                        screenNameDbModel.shopType,
                        screenNameDbModel.pageType,
                        screenNameDbModel.productId)
                if (customModel.hasCustom()) {
                    trackingOptimizerRouter?.sendTrackCustomAuth(this@SendTrackQueueService,
                            screenNameDbModel.shopId!!,
                            screenNameDbModel.shopType!!,
                            screenNameDbModel.pageType!!,
                            screenNameDbModel.productId!!)
                } else {
                    trackingOptimizerRouter?.sendTrackDefaultAuth()
                }
                if (!doMergeScreenName) {
                    sendScreenName()
                }
                deleteScreenNameJob = launch(Dispatchers.IO + TrackingExecutors.handler) {
                    trackingRepository.deleteScreenName()
                }
            }

            val eeFullModelList = trackingRepository.getAllEEFull()
            val deleteEEFullJob = launch(Dispatchers.IO + TrackingExecutors.handler) {
                trackingRepository.deleteEEFull()
            }
            eeFullModelList?.run {
                map {
                    sendTracking(it)
                }
            }
            val eeModelList = trackingRepository.getAllEE()
            val deleteEEJob = launch(Dispatchers.IO + TrackingExecutors.handler) {
                trackingRepository.deleteEE()
            }
            eeModelList?.run {
                map {
                    sendTracking(it)
                }
            }
            val regularModelList = trackingRepository.getAllRegular()
            val deleteRegularJob = launch(Dispatchers.IO + TrackingExecutors.handler) {
                trackingRepository.deleteRegular()
            }
            regularModelList?.run {
                map {
                    sendTracking(it)
                }
            }
            sendScreenName()
            deleteScreenNameJob?.join()
            deleteEEFullJob.join()
            deleteEEJob.join()
            deleteRegularJob.join()
            decreaseCounter()
            stopSelf()
        }
        return Service.START_NOT_STICKY
    }

    fun sendScreenName(){
        // send screen name individually
        if (!screenName.isNullOrEmpty()) {
            trackingOptimizerRouter?.sendScreenName(screenName!!)
            screenName = null
        }
    }

    fun decreaseCounter() {
        val value = atomicInteger.getAndDecrement()
        if (value < 0) {
            atomicInteger.set(0)
        }
    }

    fun sendTracking(it: TrackingDbModel) {
        if (trackingOptimizerRouter == null) {
            return
        }
        var hasSent = false
        val map = mutableMapOf<String, Any?>()
        val eventModel: EventModel = GsonSingleton.instance.fromJson(it.event,
                object : TypeToken<EventModel>() {}.type)
        //merge with screen name
        if (!screenName.isNullOrEmpty()) {
            map.put(EVENT, OPEN_SCREEN)
            map.put(SCREEN_NAME, screenName)
            screenName = null
        } else {
            map.put(EVENT, eventModel.event)
        }
        map.put(EVENT_CATEGORY, eventModel.category)
        map.put(EVENT_ACTION, eventModel.action)
        map.put(EVENT_LABEL, eventModel.label)
        val customDimensionMap = HashMapJsonUtil.jsonToMap(it.customDimension)
        if (customDimensionMap != null && customDimensionMap.isNotEmpty()) {
            map.putAll(customDimensionMap)
        }
        if (it is TrackingEEDbModel || it is TrackingEEFullDbModel) {
            var enhanceECommerceMap: HashMap<String, Any>? = null
            if (it is TrackingEEDbModel) {
                enhanceECommerceMap = HashMapJsonUtil.jsonToMap(it.enhanceEcommerce)
            } else if (it is TrackingEEFullDbModel) {
                enhanceECommerceMap = HashMapJsonUtil.jsonToMap(it.enhanceEcommerce)
            }
            if (enhanceECommerceMap != null && enhanceECommerceMap.isNotEmpty()) {
                map.putAll(enhanceECommerceMap)
                trackingOptimizerRouter?.sendEnhanceECommerceTracking(map)
                hasSent = true
            }
        }
        if (!hasSent) {
            trackingOptimizerRouter?.sendEventTracking(map)
        }
    }
}

