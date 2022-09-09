package com.tokopedia.search.result.product.lastfilter

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTrackingConstant
import com.tokopedia.track.TrackApp

object LastFilterTracking {
    private const val CLOSE_SAVE_LAST_FILTER = "close - save last filter"

    private fun getLastFilterEventLabel(keyword: String, filter: String): String =
        SearchEventTracking.Label.KEYWORD_FILTER.format(keyword, filter)

    @JvmStatic
    fun trackEventLastFilterClickClose(
        keyword: String,
        filter: String,
        pageSource: String,
    ) {
        val clickDataLayer = DataLayer.mapOf(
            SearchTrackingConstant.EVENT, SearchEventTracking.Event.SEARCH_RESULT,
            SearchTrackingConstant.EVENT_ACTION, CLOSE_SAVE_LAST_FILTER,
            SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
            SearchTrackingConstant.EVENT_LABEL, getLastFilterEventLabel(keyword, filter),
            SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
            SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
            SearchTrackingConstant.PAGE_SOURCE, pageSource,
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(clickDataLayer)
    }
}