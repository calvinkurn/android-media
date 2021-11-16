package com.tokopedia.play.broadcaster.ui.model.result

/**
 * Created by jegul on 03/06/20
 */
data class PageResult<T>(
        val currentValue: T,
        val state: PageResultState
) {

    companion object {

        fun<T> loading(currentValue: T) = PageResult(currentValue, PageResultState.Loading)
    }
}

sealed class PageResultState {
    data class Success(val hasNextPage: Boolean) : PageResultState()
    object Loading : PageResultState()
    data class Fail(val error: Throwable) : PageResultState()
}