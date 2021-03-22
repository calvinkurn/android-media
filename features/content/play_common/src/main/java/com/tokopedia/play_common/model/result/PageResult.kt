package com.tokopedia.play_common.model.result

/**
 * Created by jegul on 27/01/21
 */
data class PageResult<T>(
        val currentValue: T,
        val state: PageResultState
) {

    companion object {

        fun<T> Loading(currentValue: T) = PageResult(currentValue, PageResultState.Loading)
    }
}

sealed class PageResultState {
    data class Success(val pageInfo: PageInfo) : PageResultState()
    object Loading : PageResultState()
    data class Fail(val error: Throwable) : PageResultState()
}

sealed class PageInfo {

    object Unknown : PageInfo()
    data class Known(val hasNextPage: Boolean) : PageInfo()
}