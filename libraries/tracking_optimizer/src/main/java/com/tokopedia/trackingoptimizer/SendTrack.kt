package com.tokopedia.trackingoptimizer

import com.google.gson.reflect.TypeToken
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.EVENT
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.EVENT_ACTION
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.EVENT_CATEGORY
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.EVENT_LABEL
import com.tokopedia.trackingoptimizer.db.model.TrackingDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingEEDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingEEFullDbModel
import com.tokopedia.trackingoptimizer.gson.GsonSingleton
import com.tokopedia.trackingoptimizer.gson.HashMapJsonUtil
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.trackingoptimizer.repository.TrackingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by hendry on 01/03/19.
 */
var atomicInteger = AtomicInteger()

fun decreaseCounter() {
    val value = atomicInteger.getAndDecrement()
    if (value < 0) {
        atomicInteger.set(0)
    }
}

fun sendTrack(coroutineScope: CoroutineScope, trackingRepository: TrackingRepository,
              trackingOptimizerRouter: TrackingOptimizerRouter?,
              onFinished: (() -> Unit)) {
    atomicInteger.getAndIncrement()
    coroutineScope.launch {
        val eeFullModelList = trackingRepository.getAllEEFull()
        val deleteEEFullJob = launch(Dispatchers.IO + TrackingExecutors.handler) {
            trackingRepository.deleteEEFull()
        }
        eeFullModelList?.run {
            map {
                sendTrack(trackingOptimizerRouter, it)
            }
        }
        val eeModelList = trackingRepository.getAllEE()
        val deleteEEJob = launch(Dispatchers.IO + TrackingExecutors.handler) {
            trackingRepository.deleteEE()
        }
        eeModelList?.run {
            map {
                sendTrack(trackingOptimizerRouter, it)
            }
        }

        deleteEEFullJob.join()
        deleteEEJob.join()
        decreaseCounter()
        onFinished.invoke()
    }
}

fun sendTrack(trackingOptimizerRouter: TrackingOptimizerRouter?, it: TrackingDbModel) {
    if (trackingOptimizerRouter == null) {
        return
    }
    var hasSent = false
    val map = mutableMapOf<String, Any?>()
    val eventModel: EventModel = GsonSingleton.instance.fromJson(it.event,
        object : TypeToken<EventModel>() {}.type)

    map.put(EVENT, eventModel.event)
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
            trackingOptimizerRouter.sendEnhanceECommerceTracking(map)
            hasSent = true
        }
    }
    if (!hasSent) {
        trackingOptimizerRouter.sendEventTracking(map)
    }
}