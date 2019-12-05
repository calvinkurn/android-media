package com.tokopedia.trackingoptimizer.repository

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.constant.Constant
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.ECOMMERCE
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.impressionEventList
import com.tokopedia.trackingoptimizer.datasource.TrackingEEDataSource
import com.tokopedia.trackingoptimizer.db.model.TrackingEEDbModel
import com.tokopedia.trackingoptimizer.gson.GsonSingleton
import com.tokopedia.trackingoptimizer.gson.HashMapJsonUtil
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.trackingoptimizer.sendTrack


class TrackingRepository(val context: Context) : ITrackingRepository<TrackingEEDbModel> {

    val trackingEEDataSource by lazy {
        TrackingEEDataSource(context)
    }

    override fun put(map: HashMap<String, Any>?) {
        if (map == null) return
        val event = map.get(Constant.EVENT) ?: return
        val action = map.get(Constant.EVENT_ACTION) ?: return
        val category = map.get(Constant.EVENT_CATEGORY) ?: return
        val label = map.get(Constant.EVENT_LABEL) ?: return
        val ecommerceMap = HashMap<String, Any>()
        if (map.containsKey(ECOMMERCE)) {
            map.remove(Constant.EVENT)
            map.remove(Constant.EVENT_ACTION)
            map.remove(Constant.EVENT_CATEGORY)
            map.remove(Constant.EVENT_LABEL)
            val value = map.remove(Constant.ECOMMERCE)
            value?.run {
                ecommerceMap.put(ECOMMERCE, value)
            }
        }
        if (ecommerceMap.isEmpty()) {
            TrackApp.getInstance().gtm.sendGeneralEvent(map)
        } else {
            putEE(EventModel(event.toString(), category.toString(), action.toString(), label.toString()),
                map,
                ecommerceMap)
        }
    }

    override fun putEE(inputEvent: EventModel, inputCustomDimensionMap: HashMap<String, Any>?,
                       inputEnhanceECommerceMap: HashMap<String, Any>?) {
        // if it is EE click, go to Full EE, it cannot be appended.
        val isImpressionEE = inputEvent.event in impressionEventList
        if (!isImpressionEE) {
            sendTrack(inputEvent, inputCustomDimensionMap, inputEnhanceECommerceMap)
            return
        }

        // it has list? if No, put into Full EE. It cannot be appended.
        val inputList: ArrayList<Any>? = HashMapJsonUtil.findList(inputEnhanceECommerceMap)
        if (inputList == null || inputList.size == 0) {
            sendTrack(inputEvent, inputCustomDimensionMap, inputEnhanceECommerceMap)
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
        if (currentEESize >= ENHANCE_ECOMMERCE_SIZE_LIMIT) {
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
        var itemCountToAdd = (ENHANCE_ECOMMERCE_SIZE_LIMIT - currentEESize) / estimatedSizePerItem
        //can add to maximum input size
        if (itemCountToAdd > inputList.size) {
            itemCountToAdd = inputList.size
        }
        //cannot add more item to previous EE model, so we send the current DB EE and put the input EE to DB.
        if (itemCountToAdd == 0) {
            moveEETrackingToFull(trackingEEDbModel, inputEvent, inputCustomDimensionMap, inputEnhanceECommerceMap)
            return
        }
        // previous EE tracking have space, so we add to previous EE
        val inputListToAddToEE = inputList.subList(0, itemCountToAdd)
        dbList.addAll(inputListToAddToEE)

        // if there is no more input list, pout back to EE DB
        // else, then replace the old data and move to EE full
        if (itemCountToAdd == inputList.size) {
            if ((currentEESize + (estimatedSizePerItem * itemCountToAdd)) >= THRESHOLD_ECOMMERCE_SIZE_LIMIT) {
                sendTrack(
                    GsonSingleton.instance.fromJson(trackingEEDbModel.event,
                        object : TypeToken<EventModel>() {}.type),
                    inputCustomDimensionMap,
                    dbEnhanceECommerceMap)
                directPutEE(inputEvent, inputCustomDimensionMap, null)
            } else {
                directPutEE(inputEvent, inputCustomDimensionMap, dbEnhanceECommerceMap)
            }
        } else {
            sendTrack(
                GsonSingleton.instance.fromJson(trackingEEDbModel.event,
                    object : TypeToken<EventModel>() {}.type),
                inputCustomDimensionMap,
                dbEnhanceECommerceMap)
            val iter = inputList.listIterator()
            var count = 0
            while (iter.hasNext() && count < itemCountToAdd) {
                iter.remove()
                count++
            }
            // replacing old data
            // will delete old data if the input list is empty
            directPutEE(inputEvent, inputCustomDimensionMap, inputEnhanceECommerceMap)
        }

    }

    fun moveEETrackingToFull(trackingEEDbModel: TrackingEEDbModel,
                             inputEvent: EventModel, inputCustomDimension: HashMap<String, Any>?,
                             inputEnhanceECommerceMap: HashMap<String, Any>?) {
        sendTrack(trackingEEDbModel)
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
    override fun getEE(limit: Int) = trackingEEDataSource.get(limit)

    override fun deleteEE() {
        trackingEEDataSource.delete()
    }

    override fun deleteEE(eeList: List<TrackingEEDbModel>) {
        trackingEEDataSource.delete(eeList.map { it.key })
    }

    companion object {
        const val ENHANCE_ECOMMERCE_SIZE_LIMIT = 7000 // bytes
        const val THRESHOLD_ECOMMERCE_SIZE_LIMIT = 6000 // bytes
    }

}
