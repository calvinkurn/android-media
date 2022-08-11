package com.tokopedia.content.common.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on May 18, 2022
 */
sealed interface NetworkResult<T> {

    data class Success<T>(val data: T): NetworkResult<T>
    data class Error<Nothing>(val error: Throwable): NetworkResult<Nothing>
    object Loading: PagedState
    object Unknown: PagedState
}