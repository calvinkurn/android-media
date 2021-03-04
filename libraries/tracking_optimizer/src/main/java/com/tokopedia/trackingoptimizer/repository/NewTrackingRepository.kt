package com.tokopedia.trackingoptimizer.repository

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.constant.Constant
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.ECOMMERCE
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.TRACKING_QUEUE_SIZE_LIMIT_VALUE_REMOTECONFIGKEY
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.impressionEventList
import com.tokopedia.trackingoptimizer.datasource.TrackingEEDataSource
import com.tokopedia.trackingoptimizer.db.model.TrackingEEDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingEEFullDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingRegularDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingScreenNameDbModel
import com.tokopedia.trackingoptimizer.gson.HashMapJsonUtil
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.trackingoptimizer.model.ScreenCustomModel

class NewTrackingRepository(val context: Context, val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context)) : NewITrackingRepository<TrackingRegularDbModel, TrackingEEDbModel,
        TrackingEEFullDbModel, TrackingScreenNameDbModel> {

    val trackingEEDataSource by lazy {
        TrackingEEDataSource(context)
    }

    override fun putScreenName(screenName: String?) {
        if (screenName.isNullOrEmpty()) {
            return
        }
    }

    override fun putScreenName(screenName: String?, customModel: ScreenCustomModel) {
        if (screenName.isNullOrEmpty()) {
            return
        }
    }


    override fun putRegular(event: EventModel, customDimension: HashMap<String, Any>?) {
        TrackApp.getInstance().gtm.sendEvent(event.event, customDimension)
    }

    override fun put(map: HashMap<String, Any>?) {
        if (map == null) return
        val event = map.get(Constant.EVENT) ?: return
        val action = map.get(Constant.EVENT_ACTION) ?: return
        val category = map.get(Constant.EVENT_CATEGORY) ?: return
        val label = map.get(Constant.EVENT_LABEL) ?: return
        map.remove(Constant.EVENT)
        map.remove(Constant.EVENT_ACTION)
        map.remove(Constant.EVENT_CATEGORY)
        map.remove(Constant.EVENT_LABEL)
        val ecommerceMap = HashMap<String, Any>()
        if (map.containsKey(Constant.ECOMMERCE)) {
            val value = map.remove(Constant.ECOMMERCE)
            value?.run {
                ecommerceMap.put(ECOMMERCE, value)
            }
        }
        if (ecommerceMap.isEmpty()) {
            putRegular(EventModel(event.toString(), category.toString(), action.toString(), label.toString()), map)
        } else {
            putEE(EventModel(event.toString(), category.toString(), action.toString(), label.toString()),
                    map,
                    ecommerceMap)
        }
    }

    override fun putEE(map: HashMap<String, Any>?, inputEnhanceECommerceMap: HashMap<String, Any>?) {
        if (map == null) return
        val event = map.get(Constant.EVENT) ?: return
        val category = map.get(Constant.EVENT_CATEGORY) ?: return
        val action = map.get(Constant.EVENT_ACTION) ?: return
        val label = map.get(Constant.EVENT_LABEL) ?: return
        val eventModel = EventModel(event.toString(), category.toString(), action.toString(), label.toString())
        map.remove(Constant.EVENT)
        map.remove(Constant.EVENT_ACTION)
        map.remove(Constant.EVENT_CATEGORY)
        map.remove(Constant.EVENT_LABEL)
        trackingEEDataSource.put(eventModel, map, inputEnhanceECommerceMap)
    }

    override fun putEE(inputEvent: EventModel, inputCustomDimensionMap: HashMap<String, Any>?,
                       inputEnhanceECommerceMap: HashMap<String, Any>?) {
        // if it is EE click, go to Full EE, it cannot be appended.
        val isImpressionEE = inputEvent.event in impressionEventList
        if (!isImpressionEE) {
            sendData(inputEvent, inputCustomDimensionMap, inputEnhanceECommerceMap)
            return
        }

        // it has list? if No, put into Full EE. It cannot be appended.
        val inputList: ArrayList<Any>? = HashMapJsonUtil.findList(inputEnhanceECommerceMap)
        if (inputList == null || inputList.size == 0) {
            sendData(inputEvent, inputCustomDimensionMap, inputEnhanceECommerceMap)
            return
        }

        val trackingEEDbModel: TrackingEEDbModel? = trackingEEDataSource.get(inputEvent)
        if (trackingEEDbModel == null) {
            // not found in DB, then put directly.
            directPutEE(inputEvent, inputCustomDimensionMap, inputEnhanceECommerceMap)
            return
        }

        // db has model already, try to add the eecommerce
        // cek size first
        val currentEESize = trackingEEDbModel.enhanceEcommerce.length +
                trackingEEDbModel.event.length +
                trackingEEDbModel.customDimension.length

        val enhanceEcommerceSizeLimit = remoteConfig.getLong(
                TRACKING_QUEUE_SIZE_LIMIT_VALUE_REMOTECONFIGKEY,
                ENHANCE_ECOMMERCE_SIZE_LIMIT_DEFAULT).toInt()

        if (currentEESize >= enhanceEcommerceSizeLimit) {
            moveEETrackingToFull(trackingEEDbModel, inputEvent, inputCustomDimensionMap, inputEnhanceECommerceMap)
            return
        }

        val dbEnhanceECommerceMap = HashMapJsonUtil.jsonToMap(trackingEEDbModel.enhanceEcommerce)
        val dbList: ArrayList<Any>? = HashMapJsonUtil.findList(dbEnhanceECommerceMap)
        // no list found in db, put directly to DB (will replace)
        if (dbList == null || dbList.size == 0) {
            moveEETrackingToFull(trackingEEDbModel, inputEvent, inputCustomDimensionMap, inputEnhanceECommerceMap)
            return
        }

        val inputEnhanceECommerceMapString = HashMapJsonUtil.mapToJson(inputEnhanceECommerceMap)
                ?: return
        val estimatedSizePerItem = inputEnhanceECommerceMapString.length / inputList.size
        var itemCountToAdd = (enhanceEcommerceSizeLimit - currentEESize) / estimatedSizePerItem
        if (itemCountToAdd > inputList.size) {
            itemCountToAdd = inputList.size
        }
        if (itemCountToAdd == 0) {
            moveEETrackingToFull(trackingEEDbModel, inputEvent, inputCustomDimensionMap, inputEnhanceECommerceMap)
            return
        }
        // previous EE tracking have space, so we add to previous EE
        val inputListToAddToEE = inputList.subList(0, itemCountToAdd)
        dbList.addAll(inputListToAddToEE)
        inputListToAddToEE.clear()

        // if there is still input list, then replace the old data, else move it to Full DB
        if (inputList.size == 0) {
            directPutEE(inputEvent, inputCustomDimensionMap, dbEnhanceECommerceMap)
        } else {
            sendData(inputEvent, HashMapJsonUtil.jsonToMap(trackingEEDbModel.customDimension), dbEnhanceECommerceMap)
            // replacing old data
            directPutEE(inputEvent, inputCustomDimensionMap, inputEnhanceECommerceMap)
        }

    }

    fun moveEETrackingToFull(trackingEEDbModel: TrackingEEDbModel,
                             inputEvent: EventModel, inputCustomDimension: HashMap<String, Any>?,
                             inputEnhanceECommerceMap: HashMap<String, Any>?) {
        sendData(inputEvent, HashMapJsonUtil.jsonToMap(trackingEEDbModel.customDimension), HashMapJsonUtil.jsonToMap(trackingEEDbModel.enhanceEcommerce))
        directPutEE(inputEvent, inputCustomDimension, inputEnhanceECommerceMap)
    }

    fun directPutEE(inputEvent: EventModel, inputCustomDimensionMap: HashMap<String, Any>?,
                    inputEnhanceECommerceMap: HashMap<String, Any>?) {
        if (inputEnhanceECommerceMap == null && inputCustomDimensionMap!!.isEmpty()) {
            trackingEEDataSource.delete(inputEvent)
        } else {
            // will replace data, so no need delete
            trackingEEDataSource.put(inputEvent, inputCustomDimensionMap, inputEnhanceECommerceMap)
        }
    }

    override fun getAllEE() = trackingEEDataSource.getAll()

    override fun deleteEE() {
        trackingEEDataSource.delete()
    }

    companion object {
        const val ENHANCE_ECOMMERCE_SIZE_LIMIT_DEFAULT = 6700L // bytes
    }

    private fun sendData(inputEvent: EventModel, inputCustomDimensionMap: HashMap<String, Any>?,
                         inputEnhanceECommerceMap: HashMap<String, Any>?) {
        val map = mutableMapOf<String, Any?>()
        map.put(Constant.EVENT, inputEvent.event)
        map.put(Constant.EVENT_CATEGORY, inputEvent.category)
        map.put(Constant.EVENT_ACTION, inputEvent.action)
        map.put(Constant.EVENT_LABEL, inputEvent.label)
        if (inputCustomDimensionMap != null) map.putAll(inputCustomDimensionMap as Map<String, Any>)
        if (inputEnhanceECommerceMap != null) map.putAll(inputEnhanceECommerceMap as Map<String, Any>)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }
}
