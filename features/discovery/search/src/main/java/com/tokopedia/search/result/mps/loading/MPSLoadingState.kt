package com.tokopedia.search.result.mps.loading

import com.tokopedia.search.utils.mvvm.SearchUiState

data class MPSLoadingState(
    val loadingValue: Int = 0
): SearchUiState {

    fun isBelowPauseThreshold(pauseThreshold: Int) = loadingValue < pauseThreshold

    fun incrementLoading(value: Int): MPSLoadingState {
        val newLoadingValue = loadingValue + value
        return copy(loadingValue = newLoadingValue)
    }

    fun finish(): MPSLoadingState = copy(loadingValue = 100)
}
