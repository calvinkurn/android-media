package com.tokopedia.discovery.common

sealed class State<T>(val data: T? = null) {
    class Success<T>(data: T): State<T>(data)
    class Loading<T>(data: T? = null) : State<T>(data)
    class Error<T>(
        val message: String,
        data: T? = null,
        val throwable: Throwable? = null,
    ) : State<T>(data)
}
