package com.tokopedia.product.detail.postatc.tracker

import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object RecommendationTracking {
    fun onClickProductCard(
        common: CommonTracker,
        item: RecommendationItem
    ) {
        val eventAction = "click - product recommendation"

        val itemListBuilder = StringBuilder("/productafteratc")
            .append(" - ${item.pageName}")
            .append(" - rekomendasi untuk anda")
            .append(" - ${item.recommendationType}")
        if (item.isTopAds) {
            itemListBuilder.append(" - product topads")
        }
        itemListBuilder.append(" - ${common.productId}")

        val bebasOngkirValue = if (item.isFreeOngkirActive && item.labelGroupList.hasLabelGroupFulfillment()) {
            "bebas ongkir extra"
        } else if (item.isFreeOngkirActive && !item.labelGroupList.hasLabelGroupFulfillment()) {
            "bebas ongkir"
        } else {
            "none / other"
        }

        val mapEvent = hashMapOf<String, Any>(
            "event" to "productClick",
            "eventCategory" to "product detail page after atc",
            "eventAction" to eventAction,
            "eventLabel" to item.header,
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "item_list" to itemListBuilder.toString(),
            "item_list_name" to itemListBuilder.toString(),
            "productId" to common.productId,
            "ecommerce" to hashMapOf(
                "productClick" to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "index" to item.position,
                            "item_brand" to "none / other",
                            "item_category" to item.categoryBreadcrumbs.lowercase(),
                            "item_id" to item.productId,
                            "item_name" to item.name,
                            "item_variant" to "none / other",
                            "price" to item.price,
                            "dimension83" to bebasOngkirValue
                        )
                    )
                )
            ),
            "userId" to common.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun onImpressionProductCard(
        common: CommonTracker,
        item: RecommendationItem,
        trackingQueue: TrackingQueue
    ) {
        val eventAction = "click - product recommendation"

        val itemListBuilder = StringBuilder("/productafteratc")
            .append(" - ${item.pageName}")
            .append(" - rekomendasi untuk anda")
            .append(" - ${item.recommendationType}")
        if (item.isTopAds) {
            itemListBuilder.append(" - product topads")
        }
        itemListBuilder.append(" - ${common.productId}")

        val bebasOngkirValue = if (item.isFreeOngkirActive && item.labelGroupList.hasLabelGroupFulfillment()) {
            "bebas ongkir extra"
        } else if (item.isFreeOngkirActive && !item.labelGroupList.hasLabelGroupFulfillment()) {
            "bebas ongkir"
        } else {
            "none / other"
        }

        val mapEvent = hashMapOf<String, Any>(
            "event" to "productClick",
            "eventCategory" to "product detail page after atc",
            "eventAction" to eventAction,
            "eventLabel" to item.header,
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "item_list" to itemListBuilder.toString(),
            "item_list_name" to itemListBuilder.toString(),
            "productId" to common.productId,
            "ecommerce" to hashMapOf(
                "productClick" to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "index" to item.position,
                            "item_brand" to "none / other",
                            "item_category" to item.categoryBreadcrumbs.lowercase(),
                            "item_id" to item.productId,
                            "item_name" to item.name,
                            "item_variant" to "none / other",
                            "price" to item.price,
                            "dimension83" to bebasOngkirValue
                        )
                    )
                )
            ),
            "userId" to common.userId
        )

        trackingQueue.putEETracking(mapEvent)
    }
}
