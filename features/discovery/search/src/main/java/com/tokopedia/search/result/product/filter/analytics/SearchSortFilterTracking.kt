package com.tokopedia.search.result.product.filter.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTrackingConstant
import com.tokopedia.track.TrackApp

object SearchSortFilterTracking {
    private val filterTrackingData by lazy {
        FilterTrackingData(
            FilterEventTracking.Event.CLICK_SEARCH_RESULT,
            FilterEventTracking.Category.FILTER_PRODUCT,
            "",
            FilterEventTracking.Category.PREFIX_SEARCH_RESULT_PAGE
        )
    }

    @JvmStatic
    fun eventSearchResultSort(screenName: String, sortByValue: String?, userId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SORT_BY,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.SORT_BY + " - " + screenName,
                SearchTrackingConstant.EVENT_LABEL, sortByValue,
                SearchTrackingConstant.USER_ID, userId
            )
        )
    }

    @JvmStatic
    fun eventOpenFilterPage() {
        FilterTracking.eventOpenFilterPage(filterTrackingData)
    }

    @JvmStatic
    fun eventApplyFilter(
        screenName: String,
        selectedFilter: Map<String, String>?,
    ) {
        FilterTracking.eventApplyFilter(filterTrackingData, screenName, selectedFilter)
    }
}
