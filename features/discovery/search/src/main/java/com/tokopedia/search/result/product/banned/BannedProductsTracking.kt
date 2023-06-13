package com.tokopedia.search.result.product.banned

import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.track.TrackApp

object BannedProductsTracking {

    private const val IMPRESSION_BANNED_PRODUCT_TICKER_EMPTY =
        "impression - banned product ticker - empty"

    @JvmStatic
    fun trackEventImpressionBannedProductsEmptySearch(keyword: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            SearchEventTracking.Event.VIEW_SEARCH_RESULT_IRIS,
            SearchEventTracking.Category.SEARCH_RESULT,
            IMPRESSION_BANNED_PRODUCT_TICKER_EMPTY,
            keyword
        )
    }
}
