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
    }

}