package com.tokopedia.tokopedianow.recipebookmark.util

sealed class UiState<out T>(
    val data: T? = null,
    val throwable: Throwable? = null
) {
    class Success<T> (data: T?): UiState<T>(data)
    class Fail<T>(error: Throwable?): UiState<T>(throwable = error)
    class Loading<T>: UiState<T>()
}