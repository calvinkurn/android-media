package com.tokopedia.search.result.product.filter.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTrackingConstant
import com.tokopedia.search.utils.joinActiveOptionsToString
import com.tokopedia.track.TrackApp

object SearchSortFilterTracking {
    const val FILTER_COMPONENT_ID = "04.06.00.00"
    private const val FILTER_VALUE_NAME = "FilterSortPage"

    const val QUICK_FILTER_COMPONENT_ID = "04.07.00.00"
    private const val QUICK_FILTER_VALUE_NAME = "FilterQuick"

    const val DROPDOWN_QUICK_FILTER_COMPONENT_ID = "04.07.00.00"
    private const val DROPDOWN_QUICK_FILTER_VALUE_NAME = "FilterQuickDD"

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
        keyword: String,
        pageSource: String,
        selectedFilter: Map<String, String>?,
    ) {
        val searchFilters = selectedFilter?.map { with(it) { "&$key=$value" } }
            ?.joinToString("")
            .orEmpty()
        SearchSortFilterTrackingModel(
            keyword = keyword,
            valueId = "0",
            valueName = FILTER_VALUE_NAME,
            componentId = FILTER_COMPONENT_ID,
            dimension90 = pageSource,
            searchFilters = searchFilters,
        )
            .click(TrackApp.getInstance().gtm)
    }

    @JvmStatic
    fun trackEventClickQuickFilter(
        filterName: String,
        filterValue: String,
        isSelected: Boolean,
        keyword: String,
        pageSource: String,
    ) {
        if (!isSelected) return
        SearchSortFilterTrackingModel(
            keyword = keyword,
            valueId = "0",
            valueName = QUICK_FILTER_VALUE_NAME,
            componentId = QUICK_FILTER_COMPONENT_ID,
            dimension90 = pageSource,
            searchFilters = "&$filterName=$filterValue",
        )
            .click(TrackApp.getInstance().gtm)
    }

    @JvmStatic
    fun trackEventApplyDropdownQuickFilter(
        optionList: List<Option>?,
        keyword: String,
        pageSource: String,
    ) {
        SearchSortFilterTrackingModel(
            keyword = keyword,
            valueId = "0",
            valueName = DROPDOWN_QUICK_FILTER_VALUE_NAME,
            componentId = DROPDOWN_QUICK_FILTER_COMPONENT_ID,
            dimension90 = pageSource,
            searchFilters = optionList?.joinActiveOptionsToString() ?: "",
        )
            .click(TrackApp.getInstance().gtm)
    }
}
