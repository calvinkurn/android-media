package com.tokopedia.tokopedianow.recipebookmark.util

sealed class UiState<out T>(
    val data: T? = null,
    val throwable: Throwable? = null,
    val errorCode: String? = null
) {
    class Success<T>(data: T?): UiState<T>(data)
    class Fail<T>(data: T? = null, throwable: Throwable? = null, errorCode: String? = null): UiState<T>(data, throwable, errorCode)
    class Loading<T>(data: T? = null): UiState<T>(data)
}