package com.tokopedia.tokopedianow.common.model

sealed class UiState<out T> {
    data class Loading<T>(val data: T? = null): UiState<T>()
    data class Empty<T>(val data: T? = null): UiState<T>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error<T>(val data: T? = null, val throwable: Throwable) : UiState<T>()
}
