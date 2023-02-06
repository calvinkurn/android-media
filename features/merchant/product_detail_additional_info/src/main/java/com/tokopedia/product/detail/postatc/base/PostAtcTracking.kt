package com.tokopedia.product.detail.postatc.base

import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object PostAtcTracking {

    fun impressionPostAtcBottomSheet(
        trackingQueue: TrackingQueue,
        common: CommonTracker,
    ) {
        val eventAction = "impression - post atc bottomsheet"
        val mapEvent = hashMapOf<String, Any>(
            "event" to "promoView",
            "eventAction" to eventAction,
            "eventCategory" to "product detail page - post atc bottomsheet",
            "eventLabel" to "product_id:${common.productId};cart_id:${common.cartId};",
            "trackerId" to "40912",
            "businessUnit" to "product detail page",
            "component" to "comp:;temp:;elem:$eventAction;cpos:1;",
            "currentSite" to "tokopediamarketplace",
            "layout" to "layout:${common.layoutName};catName:${common.categoryName};catId:${common.categoryId};",
            "pageSource" to common.pageSource,
            "productId" to common.productId,
            "ecommerce" to hashMapOf(
                "promoView" to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "creative_name" to "null",
                            "creative_slot" to 1,
                            "item_id" to "product_id:${common.productId};cart_id:${common.cartId};",
                            "item_name" to "null"
                        )
                    )
                )
            ),
            "shopId" to common.shopId,
            "userId" to common.userId
        )
        trackingQueue.putEETracking(mapEvent)
    }

    fun sendClickLihatKeranjang(
        common: CommonTracker,
        component: ComponentTrackData
    ) {

        val eventAction = "click - lihat keranjang on post atc bottomsheet"

        val mapEvent = hashMapOf<String, Any>(
            "event" to "clickPG",
            "eventAction" to eventAction,
            "eventCategory" to "product detail page - post atc bottomsheet",
            "eventLabel" to "product_id:${common.productId};cart_id:${common.cartId};",
            "trackerId" to "40913",
            "businessUnit" to "product detail page",
            "component" to "comp:${component.name};temp:${component.type};elem:$eventAction;cpos:${component.position};",
            "currentSite" to "tokopediamarketplace",
            "layout" to "layout:${common.layoutName};catName:${common.categoryName};catId:${common.categoryId};",
            "pageSource" to common.pageSource,
            "productId" to common.productId,
            "shopId" to common.shopId,
            "userId" to common.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}
