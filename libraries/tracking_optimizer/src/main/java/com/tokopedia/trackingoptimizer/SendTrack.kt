package com.tokopedia.trackingoptimizer

import com.google.gson.reflect.TypeToken
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.EVENT
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.EVENT_ACTION
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.EVENT_CATEGORY
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.EVENT_LABEL
import com.tokopedia.trackingoptimizer.db.model.TrackingDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingEEDbModel
import com.tokopedia.trackingoptimizer.gson.GsonSingleton
import com.tokopedia.trackingoptimizer.gson.HashMapJsonUtil
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.trackingoptimizer.repository.TrackingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by hendry on 01/03/19.
 */
var atomicInteger = AtomicInteger()
private const val ROW_LIMIT = 10
private const val LOOP_LIMIT = 10

fun decreaseCounter() {
    val value = atomicInteger.getAndDecrement()
    if (value < 0) {
        atomicInteger.set(0)
    }
}

fun sendTrack(coroutineScope: CoroutineScope, trackingRepository: TrackingRepository,
              onFinished: (() -> Unit)) {
    atomicInteger.getAndIncrement()
    coroutineScope.launch {
        sendEEAllThenDelete(trackingRepository)
        decreaseCounter()
        onFinished.invoke()
    }
}

private fun sendEEAllThenDelete(trackingRepository: TrackingRepository){
    var counter = 0
    while (counter < LOOP_LIMIT) {
        val data = trackingRepository.getEE(ROW_LIMIT)
        if (data?.isNotEmpty() == true) {
            data.run {
                map {
                    sendTrack(it)
                }
            }
            trackingRepository.deleteEE(data.toList())
        } else {
            // quit loop
            break
        }
        counter++
    }

}

fun sendTrack(it: TrackingDbModel) {
    val eventModel: EventModel = GsonSingleton.instance.fromJson(it.event,
        object : TypeToken<EventModel>() {}.type)
    val customDimensionMap = HashMapJsonUtil.jsonToMap(it.customDimension)

    var enhanceECommerceMap: HashMap<String, Any>? = null
    if (it is TrackingEEDbModel) {
        enhanceECommerceMap = HashMapJsonUtil.jsonToMap(it.enhanceEcommerce)
    }
    sendTrack(eventModel, customDimensionMap, enhanceECommerceMap)
}

fun sendTrack(eventModel: EventModel, customDimensionMap:HashMap<String, Any>?,
              enhanceECommerceMap: HashMap<String, Any>?) {
    val map = mutableMapOf<String, Any?>()
    map.put(EVENT, eventModel.event)
    map.put(EVENT_CATEGORY, eventModel.category)
    map.put(EVENT_ACTION, eventModel.action)
    map.put(EVENT_LABEL, eventModel.label)
    if (customDimensionMap?.isNotEmpty() == true) {
        map.putAll(customDimensionMap)
    }
    if (enhanceECommerceMap != null && enhanceECommerceMap.isNotEmpty()) {
        map.putAll(enhanceECommerceMap)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    } else {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }
}