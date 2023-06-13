package com.tokopedia.search.result.product.inspirationlistatc.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTrackingConstant
import com.tokopedia.track.TrackApp

object InspirationListAtcTracking {

    private const val CLICK_ADD_TO_CART_CAROUSEL = "add to cart - carousel product"
    private const val SRP_SEARCH = "srp_search"

    fun trackEventClickAddToCartInspirationCarouselUnification(
        eventLabel: String,
        products: ArrayList<Any>,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.ADD_TO_CART,
                SearchTrackingConstant.EVENT_CATEGORY, SRP_SEARCH,
                SearchTrackingConstant.EVENT_ACTION, CLICK_ADD_TO_CART_CAROUSEL,
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
}
