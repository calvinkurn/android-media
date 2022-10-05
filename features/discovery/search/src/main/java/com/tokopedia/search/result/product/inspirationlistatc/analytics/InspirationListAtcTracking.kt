package com.tokopedia.search.result.product.inspirationlistatc.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.analytics.SearchTrackingConstant
import com.tokopedia.track.TrackApp

object InspirationListAtcTracking {

    fun trackEventClickAddToCartInspirationCarouselUnification(
        eventLabel: String,
        type: String,
        componentId: String,
        products: ArrayList<Any>,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.ADD_TO_CART,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_ADD_TO_CART_CAROUSEL,
                SearchTrackingConstant.EVENT_LABEL, eventLabel,
                SearchTrackingConstant.ECOMMERCE, DataLayer.mapOf("click",
                    DataLayer.mapOf(
                        "actionField", DataLayer.mapOf(
                            "list",
                            SearchTracking.getInspirationCarouselUnificationListName(
                                type,
                                componentId
                            )
                        ),
                        "products", products
                    )
                )
            )
        )
    }
}
