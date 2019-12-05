package com.tokopedia.trackingoptimizer

import com.google.gson.reflect.TypeToken
import com.tokopedia.track.TrackApp
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
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by hendry on 01/03/19.
 */
var atomicInteger = AtomicInteger()
private const val ROW_LIMIT = 5
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
        sendEEFullAllThenDelete(trackingRepository)
        sendEERegularAllThenDelete(trackingRepository)
        decreaseCounter()
        onFinished.invoke()
    }
}

private fun sendEEFullAllThenDelete(trackingRepository: TrackingRepository){
    var counter = 0
    while (counter < LOOP_LIMIT) {
        val data = trackingRepository.getEEFull(ROW_LIMIT)
        if (data?.isNotEmpty() == true) {
            data.run {
                map {
                    sendTrack(it)
                }
            }
            trackingRepository.deleteEEFull(data.toList())
        } else {
            // quit loop
            break
        }
        counter++
    }
}

private fun sendEERegularAllThenDelete(trackingRepository: TrackingRepository){
    var counter = 0
    while (counter < LOOP_LIMIT) {
        val data = trackingRepository.getRegular(ROW_LIMIT)
        if (data?.isNotEmpty() == true) {
            data.run {
                map {
                    sendTrack(it)
                }
            }
            trackingRepository.deleteRegular(data.toList())
        } else {
            // quit loop
            break
        }
        counter++
    }
}

private fun sendTrack(it: TrackingDbModel) {
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
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
            hasSent = true
        }
    }
    if (!hasSent) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }
}