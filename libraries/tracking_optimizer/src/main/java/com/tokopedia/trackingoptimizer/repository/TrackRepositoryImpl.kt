package com.tokopedia.trackingoptimizer.repository

import com.tokopedia.trackingoptimizer.model.EventModel

interface TrackRepositoryImpl<T, U, V, W> {

    fun put(map: HashMap<String, Any>?)

    fun putRegular(
        event: EventModel,
        customDimension: HashMap<String, Any>?
    )

    fun putEE(
        inputEvent: EventModel,
        inputCustomDimensionMap: HashMap<String, Any>?,
        inputEnhanceECommerceMap: HashMap<String, Any>?
    )

    fun putEE(
        map: HashMap<String, Any>?,
        inputEnhanceECommerceMap: HashMap<String, Any>?
    )

    fun getAllEE(): Array<U>?
    fun deleteEE()
    fun hasDataToSend(): Boolean
}
