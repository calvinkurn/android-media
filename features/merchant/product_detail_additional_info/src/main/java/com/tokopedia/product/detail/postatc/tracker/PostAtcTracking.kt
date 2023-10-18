package com.tokopedia.product.detail.postatc.tracker

import com.tokopedia.product.detail.postatc.base.ComponentTrackData
import com.tokopedia.product.detail.postatc.data.model.PostAtcInfo
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object PostAtcTracking {

    fun impressionPostAtcBottomSheet(
        trackingQueue: TrackingQueue,
        userId: String,
        postAtcInfo: PostAtcInfo
    ) {
        val eventAction = "impression - post atc bottomsheet"
        val mapEvent = hashMapOf<String, Any>(
            "event" to "promoView",
            "eventAction" to eventAction,
            "eventCategory" to "product detail page - post atc bottomsheet",
            "eventLabel" to "product_id:${postAtcInfo.productId};cart_id:${postAtcInfo.cartId};",
            "trackerId" to "40912",
            "businessUnit" to "product detail page",
            "component" to "comp:;temp:;elem:$eventAction;cpos:1;",
            "currentSite" to "tokopediamarketplace",
            "layout" to "layout:${postAtcInfo.layoutName};catName:${postAtcInfo.categoryName};catId:${postAtcInfo.categoryId};",
            "pageSource" to postAtcInfo.pageSource,
            "productId" to postAtcInfo.productId,
            "ecommerce" to hashMapOf(
                "promoView" to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "creative_name" to "null",
                            "creative_slot" to 1,
                            "item_id" to "product_id:${postAtcInfo.productId};cart_id:${postAtcInfo.cartId};",
                            "item_name" to "null"
                        )
                    )
                )
            ),
            "shopId" to postAtcInfo.shopId,
            "userId" to userId
        )
        trackingQueue.putEETracking(mapEvent)
    }

    fun sendClickLihatKeranjang(
        userId: String,
        postAtcInfo: PostAtcInfo,
        component: ComponentTrackData
    ) {
        val eventAction = "click - lihat keranjang on post atc bottomsheet"

        val mapEvent = hashMapOf<String, Any>(
            "event" to "clickPG",
            "eventAction" to eventAction,
            "eventCategory" to "product detail page - post atc bottomsheet",
            "eventLabel" to "product_id:${postAtcInfo.productId};cart_id:${postAtcInfo.cartId};",
            "trackerId" to "40913",
            "businessUnit" to "product detail page",
            "component" to "comp:${component.name};temp:${component.type};elem:$eventAction;cpos:${component.position};",
            "currentSite" to "tokopediamarketplace",
            "layout" to "layout:${postAtcInfo.layoutName};catName:${postAtcInfo.categoryName};catId:${postAtcInfo.categoryId};",
            "pageSource" to postAtcInfo.pageSource,
            "productId" to postAtcInfo.productId,
            "shopId" to postAtcInfo.shopId,
            "userId" to userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}
