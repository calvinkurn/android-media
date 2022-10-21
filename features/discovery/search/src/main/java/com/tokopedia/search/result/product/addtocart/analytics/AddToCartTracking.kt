package com.tokopedia.search.result.product.addtocart.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTrackingConstant
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.addtocart.analytics.AddToCartTracking.ADD_TO_CART_PRODUCT
import com.tokopedia.search.result.product.addtocart.analytics.AddToCartTracking.ADD_TO_CART_PRODUCT_TOPADS
import com.tokopedia.track.TrackApp

object AddToCartTracking {
    private const val SRP_SEARCH = "srp_search"
    private const val ADD_TO_CART_PRODUCT = "add to cart - product"
    private const val ADD_TO_CART_PRODUCT_TOPADS = "add to cart - product - topads"

    fun trackEventClickAddToCart(
        eventLabel: String,
        isAds: Boolean,
        products: ArrayList<Any>,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.ADD_TO_CART,
                SearchTrackingConstant.EVENT_CATEGORY, SRP_SEARCH,
                SearchTrackingConstant.EVENT_ACTION, createATCSuccessEventAction(isAds),
                SearchTrackingConstant.EVENT_LABEL, eventLabel,
                SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
                SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                SearchTrackingConstant.ECOMMERCE, DataLayer.mapOf(
                    SearchEventTracking.ECommerce.ADD, DataLayer.mapOf(
                        SearchEventTracking.ECommerce.PRODUCTS, products
                    )
                )
            )
        )
    }

    private fun createATCSuccessEventAction(isAds: Boolean) =
        if (isAds)
            ADD_TO_CART_PRODUCT_TOPADS
        else
            ADD_TO_CART_PRODUCT
}
