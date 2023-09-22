package com.tokopedia.search.result.product.filter.analytics

import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.search.utils.joinActiveOptionsToString
import com.tokopedia.track.TrackApp

object SearchSortFilterTracking {
    const val FILTER_COMPONENT_ID = "04.06.00.00"
    private const val FILTER_VALUE_NAME = "FilterSortPage"

    const val QUICK_FILTER_COMPONENT_ID = "04.07.00.00"
    private const val QUICK_FILTER_VALUE_NAME = "FilterQuick"

    const val DROPDOWN_QUICK_FILTER_COMPONENT_ID = "04.07.00.00"
    private const val DROPDOWN_QUICK_FILTER_VALUE_NAME = "FilterQuickDD"

    const val SORT_COMPONENT_ID = "04.10.00.00"
    private const val SORT_VALUE_NAME = "SortPage"

    private val filterTrackingData by lazy {
        FilterTrackingData(
            FilterEventTracking.Event.CLICK_SEARCH_RESULT,
            FilterEventTracking.Category.FILTER_PRODUCT,
            "",
            FilterEventTracking.Category.PREFIX_SEARCH_RESULT_PAGE
        )
    }

    @JvmStatic
    fun eventOpenFilterPage() {
        FilterTracking.eventOpenFilterPage(filterTrackingData)
    }

    private fun Map<String, String>?.toSearchFilterString() = this?.map { with(it) { "&$key=$value" } }
        ?.joinToString("")
        .orEmpty()

    @JvmStatic
    fun eventApplyFilter(
        keyword: String,
        pageSource: String,
        selectedSort: Map<String, String>?,
        selectedFilter: Map<String, String>?,
        sortApplyFilter: Map<String, String>?,
    ) {
        val searchSorts = selectedSort.toSearchFilterString()
        val applyFilters = sortApplyFilter.toSearchFilterString()
        val searchFilters = selectedFilter.toSearchFilterString()
        SearchSortFilterTrackingModel(
            keyword = keyword,
            valueId = "0",
            valueName = FILTER_VALUE_NAME,
            componentId = FILTER_COMPONENT_ID,
            dimension90 = pageSource,
            searchFilters = searchSorts+applyFilters+searchFilters,
        )
            .click(TrackApp.getInstance().gtm)
    }

    @JvmStatic
    fun eventApplySort(
        keyword: String,
        pageSource: String,
        selectedSort: Map<String, String>?,
    ) {
        val searchSorts = selectedSort.toSearchFilterString()

        SearchSortFilterTrackingModel(
            keyword = keyword,
            valueId = "0",
            valueName = SORT_VALUE_NAME,
            componentId = SORT_COMPONENT_ID,
            dimension90 = pageSource,
            searchFilters = searchSorts,
        ).click(TrackApp.getInstance().gtm)
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
        val searchFilter = mapOf(filterName to filterValue).toSearchFilterString()
        SearchSortFilterTrackingModel(
            keyword = keyword,
            valueId = "0",
            valueName = QUICK_FILTER_VALUE_NAME,
            componentId = QUICK_FILTER_COMPONENT_ID,
            dimension90 = pageSource,
            searchFilters = searchFilter,
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
