package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import android.os.Bundle
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp

object ViewToViewBottomSheetTracker {

    fun eventProductImpress(
        product: RecommendationItem,
        headerTitle: String,
        userId: String,
        position: Int,
        anchorProductId: String,
    ) {

        val itemBundle = Bundle().apply {
            putString("event", "view_item_list")
            putString("eventAction", "impression on product bottom sheet v2v widget")
            putString("eventCategory", "product detail page")
            putString("eventLabel", headerTitle)
            putString("businessUnit", "home & browse")
            putString("currentSite", "tokopediamarketplace")
            putString("trackerId", "40440")
            putString("item_list", product.asItemList())

            //promotion
            val bundlePromotion = product.asBundle(product.position)
            val list = arrayListOf(bundlePromotion)
            putParcelableArrayList("items", list)

            putString("productId", anchorProductId)
            putString("userId", userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent("addToCart", itemBundle)
    }

    fun eventProductClick(
        product: RecommendationItem,
        headerTitle: String,
        userId: String,
        position: Int,
        anchorProductId: String,
    ) {
        val itemBundle = Bundle().apply {
            putString("event", "select_content")
            putString("eventAction", "click on product bottom sheet v2v widget")
            putString("eventCategory", "product detail page")
            putString("eventLabel", headerTitle)
            putString("businessUnit", "home & browse")
            putString("currentSite", "tokopediamarketplace")
            putString("trackerId", "40442")
            putString("item_list", product.asItemList())

            //promotion
            val bundlePromotion = product.asBundle(position)
            val list = arrayListOf(bundlePromotion)
            putParcelableArrayList("items", list)

            putString("productId", anchorProductId)
            putString("userId", userId)

        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent("productClick", itemBundle)
    }

    fun eventAddToCart(
        product: RecommendationItem,
        headerTitle: String,
        userId: String,
        anchorProductId: String,
    ) {
        val itemBundle = Bundle().apply {
            putString("event", "add_to_cart")
            putString("eventAction", "click keranjang on product bottom sheet v2v widget")
            putString("eventCategory", "product detail page")
            putString("eventLabel", headerTitle)
            putString("businessUnit", "home & browse")
            putString("currentSite", "tokopediamarketplace")
            putString("trackerId", "40443")
            putString("item_list", product.asItemList())

            //promotion
            val bundlePromotion = product.asBundle(product.position)
            val list = arrayListOf(bundlePromotion)
            putParcelableArrayList("items", list)

            putString("productId", anchorProductId)
            putString("userId", userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent("addToCart", itemBundle)
    }

    private fun RecommendationItem.asItemList(): String {
        val isTopAds = if (isTopAds) "topads" else "nontopads"
        return "/product - v2v widget - rekomendasi untuk anda - $recommendationType - product $isTopAds"
    }

    private fun RecommendationItem.asBundle(position: Int) : Bundle {
        return Bundle().apply {
            putInt("category_id" , departmentId)
            putString("dimension40", asItemList())
            putString("dimension45", cartId)
            putString("item_brand", "null")
            putString("item_category", categoryBreadcrumbs)
            putLong("item_id", productId)
            putInt("price", priceInt)
            putInt("quantity", quantity)
            putInt("shop_id", shopId)
            putString("shop_name", shopName)
            putString("shop_type", shopType)
            putInt("position", position)
        }
    }
}
