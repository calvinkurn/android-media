package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by yovi.putra on 25/01/23"
 * Project name: android-tokopedia-core
 * MyNakama: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4125
 **/
object BMGMTracking {

    fun onClicked(
        title: String,
        commonTracker: CommonTracker,
        component: ComponentTrackDataModel?,
        trackingQueue: TrackingQueue
    ) {
        val action = "click - bmgm component"
        val event = "promoClick"
        val mapEvent = hashMapOf<String, Any>(
            "event" to event,
            "eventCategory" to "product detail page",
            "eventAction" to action,
            "eventLabel" to "",
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "trackerId" to "45682",
            "productId" to commonTracker.productId,
            "layout" to TrackingUtil.generateLayoutValue(productInfo = commonTracker.productInfo),
            "component" to component?.getComponentData(action).orEmpty(),
            "ecommerce" to hashMapOf(
                event to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "creative_name" to "null",
                            "creative_slot" to "null",
                            "item_id" to "product_id:${commonTracker.productId}",
                            "item_name" to "title:$title"
                        )
                    )
                )
            ),
            "shopId" to commonTracker.shopId,
            "userId" to commonTracker.userId
        )

        trackingQueue.putEETracking(mapEvent)
    }
}
