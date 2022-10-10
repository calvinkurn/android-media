package com.tokopedia.search.result.product.inspirationlistatc.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTrackingConstant
import com.tokopedia.track.TrackApp

object InspirationListAtcTracking {

    fun trackEventClickAddToCartInspirationCarouselUnification(
        eventLabel: String,
        products: ArrayList<Any>,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.ADD_TO_CART,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SRP_SEARCH,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_ADD_TO_CART_CAROUSEL,
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
