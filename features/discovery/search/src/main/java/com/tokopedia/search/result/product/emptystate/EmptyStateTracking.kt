package com.tokopedia.search.result.product.emptystate

import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.track.TrackApp

object EmptyStateTracking {

    private const val CLICK_CHANGE_KEYWORD = "click ganti kata kunci"

    @JvmStatic
    fun eventUserClickNewSearchOnEmptySearchProduct(keyword: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            SearchEventTracking.Event.SEARCH_RESULT,
            SearchEventTracking.Category.SEARCH_RESULT,
            CLICK_CHANGE_KEYWORD,
            keyword
        )
    }
}