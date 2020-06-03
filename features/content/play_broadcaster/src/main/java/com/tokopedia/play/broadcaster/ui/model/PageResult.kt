package com.tokopedia.play.broadcaster.ui.model

/**
 * Created by jegul on 03/06/20
 */
data class PageResult<T>(
        val currentValue: T,
        val state: ResultState
) {

    companion object {

        fun<T> Loading(currentValue: T) = PageResult(currentValue, ResultState.Loading)
    }
}

sealed class ResultState {
    data class Success(val hasNextPage: Boolean) : ResultState()
    object Loading : ResultState()
    data class Fail(val error: Throwable) : ResultState()
}