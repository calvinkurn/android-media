package com.tokopedia.trackingoptimizer.repository

import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.trackingoptimizer.model.ScreenCustomModel

interface ITrackingRepository<T, U, V, W> {

    fun putScreenName(screenName: String?)
    fun putScreenName(screenName: String?, customModel: ScreenCustomModel)

    fun put(map: HashMap<String, Any>?)

    fun putRegular(event: EventModel,
                   customDimension: HashMap<String, Any>?)

    fun putEE(inputEvent: EventModel,
              inputCustomDimensionMap: HashMap<String, Any>?,
              inputEnhanceECommerceMap: HashMap<String, Any>?)

    fun putEE(map: HashMap<String, Any>?,
              inputEnhanceECommerceMap: HashMap<String, Any>?)

    fun getAllRegular(): Array<T>?
    fun getAllEE(): Array<U>?
    fun getAllEEFull(): Array<V>?
    fun getAllScreenName(): Array<W>?

    fun getRegular(limit:Int): Array<T>?
    fun getEE(limit:Int): Array<U>?
    fun getEEFull(limit:Int): Array<V>?

    fun deleteRegular()
    fun deleteEE()
    fun deleteEEFull()
    fun deleteRegular(keyList:List<T>)
    fun deleteEE(keyList:List<U>)
    fun deleteEEFull(keyList:List<V>)
    fun deleteScreenName()
}
