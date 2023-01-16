package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.builder.Tracker

object ViewToViewBottomSheetTracker {

    fun eventProductImpress(
        product: RecommendationItem,
        headerTitle: String,
        userId: String,
        position: Int,
    ) {
        Tracker.Builder()
            .setEvent("view_item_list")
            .setEventAction("impression on product bottom sheet v2v widget")
            .setEventCategory("product detail page")
            .setEventLabel(headerTitle)
            .setCustomProperty("trackerId", "40440")
            .setBusinessUnit("home & browse")
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty("item_list", product.asItemList())
            .setCustomProperty("items", product.asBundle(position))
            .setCustomProperty("productId", product.productId)
            .setUserId(userId)
            .build()
            .send()
    }

    fun eventProductClick(
        product: RecommendationItem,
        headerTitle: String,
        userId: String,
        position: Int,
    ) {
        Tracker.Builder()
            .setEvent("select_content")
            .setEventAction("click on product bottom sheet v2v widget")
            .setEventCategory("product detail page")
            .setEventLabel(headerTitle)
            .setCustomProperty("trackerId", "40442")
            .setBusinessUnit("home & browse")
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty("item_list", product.asItemList())
            .setCustomProperty("items", product.asBundle(position))
            .setCustomProperty("productId", product.productId)
            .setUserId(userId)
            .build()
            .send()
    }

    fun eventAddToCart(
        product: RecommendationItem,
        headerTitle: String,
        userId: String,
        position: Int,
    ) {
        Tracker.Builder()
            .setEvent("add_to_cart")
            .setEventAction("click keranjang on product bottom sheet v2v widget")
            .setEventCategory("product detail page")
            .setEventLabel(headerTitle)
            .setCustomProperty("trackerId", "40443")
            .setBusinessUnit("home & browse")
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty("items", product.asBundle(position))
            .setCustomProperty("productId", product.productId)
            .setUserId(userId)
            .build()
            .send()
    }

    private fun RecommendationItem.asItemList(): String {
        val isTopAds = if (isTopAds) "topads" else "nontopads"
        return "/product - v2v widget - rekomendasi untuk anda - $type - product $isTopAds"
    }

    private fun RecommendationItem.asBundle(position: Int) : Map<String, Any> {
        return mapOf(
            "category_id" to departmentId,
            "dimension40" to this.asItemList(),
            "dimension45" to cartId,
            "item_brand" to "",
            "item_category" to categoryBreadcrumbs,
            "item_id" to productId,
            "item_name" to name,
            "price" to price,
            "quantity" to quantity,
            "shop_id" to shopId,
            "shop_name" to shopName,
            "shop_type" to shopType,
            "position" to position,
        )
    }
}
