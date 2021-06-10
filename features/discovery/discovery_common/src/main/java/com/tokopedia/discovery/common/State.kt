package com.tokopedia.discovery.common

sealed class State<T>(
        val data: T? = null,
        val message: String? = null,
        val throwable: Throwable? = null
) {
    class Success<T>(data: T): State<T>(data)
    class Loading<T>(data: T? = null) : State<T>(data)
    class Error<T>(message: String, data: T? = null, throwable: Throwable? = null) : State<T>(data, message, throwable)
}