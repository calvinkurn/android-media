package com.tokopedia.createpost.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
sealed interface PagedState {

    data class Success(val hasNextPage: Boolean, val nextCursor: String): PagedState
    data class Error(val error: Throwable): PagedState
    object Loading: PagedState
    object Unknown: PagedState

    val isLoading: Boolean
        get() = this == Loading

    val isNextPage: Boolean
        get() = (this is Success && this.hasNextPage) || this !is Success
}