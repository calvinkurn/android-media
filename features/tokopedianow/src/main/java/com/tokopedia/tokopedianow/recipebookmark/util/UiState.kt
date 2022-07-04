package com.tokopedia.tokopedianow.recipebookmark.util

sealed class UiState<out T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T> (data: T?): UiState<T>(data)
    class Fail<T>(message: String?, data: T? = null): UiState<T>(data, message)
    class Loading<T>: UiState<T>()
    class Empty<T>: UiState<T>()
}