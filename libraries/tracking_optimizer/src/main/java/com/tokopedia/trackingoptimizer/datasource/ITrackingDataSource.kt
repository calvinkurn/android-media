package com.tokopedia.trackingoptimizer.datasource

import com.tokopedia.trackingoptimizer.model.EventModel

interface ITrackingDataSource<U : Any> {

    fun put(event: EventModel,
            customDimension: HashMap<String, Any>? = null,
            enhanceECommerceMap: HashMap<String, Any>? = null)

    fun put(model: U)

    fun get(event: EventModel): U?

    fun getAll(): Array<U>?

    fun delete(event: EventModel)

    fun delete()

}
