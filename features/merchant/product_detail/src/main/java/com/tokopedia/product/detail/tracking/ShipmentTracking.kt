package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object ShipmentTracking {
    fun sendImpressionScheduledDeliveryComponent(
        trackingQueue: TrackingQueue,
        labels: List<String>,
        common: CommonTracker,
        component: ComponentTrackDataModel
    ) {
        val eventAction = "impression - scheduled delivery component"
        val combinedLabels = labels.joinToString(", ")
        val mapEvent = hashMapOf<String, Any>(
            "event" to "promoView",
            "eventAction" to eventAction,
            "eventCategory" to "product detail page",
            "eventLabel" to "",
            "trackerId" to "40897",
            "businessUnit" to "product detail page",
            "component" to "comp:${component.componentName};temp:${component.componentType};elem:$eventAction;cpos:${component.adapterPosition};",
            "currentSite" to "tokopediamarketplace",
            "layout" to "layout:${common.layoutName};catName:${common.categoryName};catId:${common.categoryId};",
            "productId" to common.productId,
            "ecommerce" to hashMapOf(
                "promoView" to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "creative_name" to "null",
                            "creative_slot" to component.adapterPosition,
                            "item_id" to "null",
                            "item_name" to "label:$combinedLabels;"
                        )
                    )
                )
            ),
            "shopId" to common.shopId,
            "userId" to common.userId
        )
        trackingQueue.putEETracking(mapEvent)
    }

    fun sendClickLihatJadwalLainnyaOnScheduleDelivery(
        labels: List<String>,
        common: CommonTracker,
        component: ComponentTrackDataModel
    ) {
        val eventAction = "click - lihat jadwal lainnya on schedule delivery"
        val combinedLabels = labels.joinToString(", ")
        val mapEvent = hashMapOf<String, Any>(
            "event" to "clickPG",
            "eventAction" to "click - lihat jadwal lainnya on schedule delivery",
            "eventCategory" to "product detail page",
            "eventLabel" to "label:$combinedLabels;",
            "trackerId" to "40898",
            "businessUnit" to "product detail page",
            "component" to "comp:${component.componentName};temp:${component.componentType};elem:$eventAction;cpos:${component.adapterPosition};",
            "currentSite" to "tokopediamarketplace",
            "layout" to "layout:${common.layoutName};catName:${common.categoryName};catId:${common.categoryId};",
            "productId" to common.productId,
            "shopId" to common.shopId,
            "userId" to common.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}
