package com.tokopedia.product.detail.postatc.tracker

import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.trackingoptimizer.TrackingQueue

object RecommendationTracking {
    fun onClickProductCard(
        productId: String,
        userId: String,
        isLoggedIn: Boolean,
        item: RecommendationItem,
        trackingQueue: TrackingQueue
    ) {
        val eventAction = "click - product recommendation"

        val postFixLogin = " - non login".takeIf { !isLoggedIn } ?: ""

        val itemListBuilder = StringBuilder("/productafteratc").apply {
            append(" - ${item.pageName}")
            append(" - rekomendasi untuk anda")
            append(postFixLogin)
            append(" - ${item.recommendationType}")

            if (item.isTopAds) {
                append(" - product topads")
            }

            append(" - $productId")
        }

        val bebasOngkirValue =
            if (item.isFreeOngkirActive && item.labelGroupList.hasLabelGroupFulfillment()) {
                "bebas ongkir extra"
            } else if (item.isFreeOngkirActive && !item.labelGroupList.hasLabelGroupFulfillment()) {
                "bebas ongkir"
            } else {
                "none / other"
            }

        val mapEvent = hashMapOf<String, Any>(
            "event" to "productClick",
            "eventCategory" to "product detail page after atc",
            "eventAction" to eventAction + postFixLogin,
            "eventLabel" to item.header,
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "ecommerce" to hashMapOf(
                "click" to hashMapOf(
                    "actionField" to hashMapOf(
                        "list" to itemListBuilder.toString()
                    ),
                    "products" to arrayListOf(
                        hashMapOf(
                            "index" to item.position,
                            "item_brand" to "none / other",
                            "item_category" to item.categoryBreadcrumbs.lowercase(),
                            "item_id" to item.productId,
                            "item_name" to item.name,
                            "item_variant" to "none / other",
                            "price" to removeCurrencyPrice(item.price),
                            "dimension83" to bebasOngkirValue
                        )
                    )
                )
            ),
            "userId" to userId
        )

        trackingQueue.putEETracking(mapEvent)
    }

    fun onImpressionProductCard(
        productId: String,
        userId: String,
        isLoggedIn: Boolean,
        item: RecommendationItem,
        trackingQueue: TrackingQueue
    ) {
        val eventAction = "impression - product recommendation"

        val postFixLogin = " - non login".takeIf { !isLoggedIn } ?: ""

        val itemListBuilder = StringBuilder("/productafteratc").apply {
            append(" - ${item.pageName}")
            append(" - rekomendasi untuk anda")
            append(postFixLogin)
            append(" - ${item.recommendationType}")

            if (item.isTopAds) {
                append(" - product topads")
            }

            append(" - ${productId}")
        }

        val bebasOngkirValue =
            if (item.isFreeOngkirActive && item.labelGroupList.hasLabelGroupFulfillment()) {
                "bebas ongkir extra"
            } else if (item.isFreeOngkirActive && !item.labelGroupList.hasLabelGroupFulfillment()) {
                "bebas ongkir"
            } else {
                "none / other"
            }

        val mapEvent = hashMapOf<String, Any>(
            "event" to "productView",
            "eventCategory" to "product detail page after atc",
            "eventAction" to eventAction + postFixLogin,
            "eventLabel" to item.header,
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "ecommerce" to hashMapOf(
                "currencyCode" to "IDR",
                "impressions" to arrayListOf(
                    hashMapOf(
                        "name" to item.name,
                        "id" to item.productId.toString(),
                        "price" to removeCurrencyPrice(item.price),
                        "brand" to "none / other",
                        "category" to item.categoryBreadcrumbs.lowercase(),
                        "variant" to "none / other",
                        "list" to itemListBuilder.toString(),
                        "position" to item.position,
                        "dimension83" to bebasOngkirValue
                    )
                )
            ),
            "userId" to userId
        )

        trackingQueue.putEETracking(mapEvent)
    }

    private fun removeCurrencyPrice(priceFormatted: String): String {
        return priceFormatted.replace("\\D".toRegex(), "")
    }
}
