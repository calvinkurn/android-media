package com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel

class TokofoodSearchUiState(
    val state: Int,
    val data: Any? = null,
    val throwable: Throwable? = null
) {

    companion object {
        const val STATE_LOAD_INITIAL = 0
        const val STATE_LOAD_MORE = 1
        const val STATE_SUCCESS_LOAD_INITIAL = 2
        const val STATE_SUCCESS_LOAD_MORE = 3
        const val STATE_EMPTY = 4
        const val STATE_ERROR_INITIAL = 5
        const val STATE_ERROR_LOAD_MORE = 6
    }

}