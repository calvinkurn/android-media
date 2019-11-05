package com.tokopedia.trackingoptimizer.repository

import com.tokopedia.trackingoptimizer.model.EventModel

interface ITrackingRepository<U> {

    fun put(map: HashMap<String, Any>?)

    fun putEE(inputEvent: EventModel,
              inputCustomDimensionMap: HashMap<String, Any>?,
              inputEnhanceECommerceMap: HashMap<String, Any>?)

    fun getAllEE(): Array<U>?

    fun getEE(limit:Int): Array<U>?

    fun deleteEE()
    fun deleteEE(keyList:List<U>)
}
