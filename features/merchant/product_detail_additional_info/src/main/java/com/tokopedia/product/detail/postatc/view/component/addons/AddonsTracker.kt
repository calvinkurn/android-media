package com.tokopedia.product.detail.postatc.view.component.addons

import com.tokopedia.product.detail.postatc.data.model.PostAtcInfo
import com.tokopedia.trackingoptimizer.TrackingQueue

class AddonsTracker {
    data class AddonsItem(
        val isChecked: Boolean,
        val subtitle: String,
        val position: Int,
        val title: String
    )
}

object AddonsTracking {

    fun onImpressAddonsItem(
        info: PostAtcInfo,
        item: AddonsTracker.AddonsItem,
        userId: String,
        trackingQueue: TrackingQueue
    ) {
        val mapEvent = hashMapOf<String, Any>(
            "event" to "promoView",
            "eventAction" to "impression - post atc add on",
            "eventCategory" to "product detail page - post atc",
            "eventLabel" to "",
            "trackerId" to "44807",
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "productId" to info.productId,
            "ecommerce" to hashMapOf(
                "promoView" to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "creative_name" to "subtitle:${item.subtitle};price:;",
                            "creative_slot" to "position:${item.position}",
                            "item_id" to "${info.cartId} - ${info.productId}",
                            "item_name" to "title:${item.title};check:${item.isChecked}"
                        )
                    )
                )
            ),
            "shopId" to info.shopId,
            "userId" to userId
        )
        trackingQueue.putEETracking(mapEvent)
    }

    fun onClickAddonsItem(
        info: PostAtcInfo,
        item: AddonsTracker.AddonsItem,
        userId: String,
        trackingQueue: TrackingQueue
    ) {
        val mapEvent = hashMapOf<String, Any>(
            "event" to "promoClick",
            "eventAction" to "click - post atc add on",
            "eventCategory" to "product detail page - post atc",
            "eventLabel" to item.isChecked,
            "trackerId" to "44808",
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "productId" to info.productId,
            "ecommerce" to hashMapOf(
                "promoClick" to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "creative_name" to "subtitle:${item.subtitle};price:;",
                            "creative_slot" to "position:${item.position}",
                            "item_id" to "${info.cartId} - ${info.productId}",
                            "item_name" to "title:${item.title}"
                        )
                    )
                )
            ),
            "shopId" to info.shopId,
            "userId" to userId
        )
        trackingQueue.putEETracking(mapEvent)
    }

    fun onClickAddonsInfo(
        info: PostAtcInfo,
        item: AddonsTracker.AddonsItem,
        userId: String,
        trackingQueue: TrackingQueue
    ) {
        val mapEvent = hashMapOf<String, Any>(
            "event" to "promoClick",
            "eventAction" to "click - add ons info component button",
            "eventCategory" to "product detail page - post atc",
            "eventLabel" to "",
            "trackerId" to "45029",
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "productId" to info.productId,
            "ecommerce" to hashMapOf(
                "promoClick" to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "creative_name" to "subtitle:${item.subtitle}",
                            "creative_slot" to "position:${item.position}",
                            "item_id" to "${info.cartId} - ${info.productId}",
                            "item_name" to "title:${item.title}"
                        )
                    )
                )
            ),
            "shopId" to info.shopId,
            "userId" to userId
        )
        trackingQueue.putEETracking(mapEvent)
    }
}
