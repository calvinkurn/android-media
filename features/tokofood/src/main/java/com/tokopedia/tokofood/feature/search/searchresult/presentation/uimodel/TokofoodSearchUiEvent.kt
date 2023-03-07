package com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel

class TokofoodSearchUiEvent(
    val state: Int,
    val data: Any? = null,
    val throwable: Throwable? = null
) {

    companion object {
        const val EVENT_OPEN_DETAIL_BOTTOMSHEET = 0
        const val EVENT_SUCCESS_LOAD_DETAIL_FILTER = 1
        const val EVENT_FAILED_LOAD_DETAIL_FILTER = 2
        const val EVENT_OPEN_QUICK_SORT_BOTTOMSHEET = 3
        const val EVENT_OPEN_QUICK_FILTER_PRICE_RANGE_BOTTOMSHEET = 4
        const val EVENT_OPEN_QUICK_FILTER_NORMAL_BOTTOMSHEET = 5
        const val EVENT_FAILED_LOAD_MORE = 6
        const val EVENT_FAILED_LOAD_FILTER = 7
        const val EVENT_FAILED_LOAD_SEARCH_RESULT = 8
        const val EVENT_SUCCESS_EDIT_PINPOINT = 9
        const val EVENT_FAILED_EDIT_PINPOINT = 10
    }

}