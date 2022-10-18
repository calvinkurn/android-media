package com.tokopedia.search.result.product.addtocart.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTrackingConstant
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.track.TrackApp

object AddToCartTracking {
    private const val SRP_SEARCH = "srp_search"
    const val ADD_TO_CART_PRODUCT = "add to cart - product"
    const val ADD_TO_CART_PRODUCT_TOPADS = "add to cart - product - topads"

    fun trackEventClickAddToCart(
        eventLabel: String,
        eventAction: String,
        products: ArrayList<Any>,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.ADD_TO_CART,
                SearchTrackingConstant.EVENT_CATEGORY, SRP_SEARCH,
                SearchTrackingConstant.EVENT_ACTION, eventAction,
                SearchTrackingConstant.EVENT_LABEL, eventLabel,
                SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
                SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                SearchTrackingConstant.ECOMMERCE, DataLayer.mapOf(
                    "add", DataLayer.mapOf(
                        "products", products
                    )
                )
            )
        )
    }
}
