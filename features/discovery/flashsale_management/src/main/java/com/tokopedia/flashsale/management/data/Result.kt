package com.tokopedia.flashsale.management.data

sealed class Result<out T: Any>
data class Success<out T: Any>(val response: T): Result<T>()
data class ResponseError(val error: List<String>): Result<Nothing>()
data class RequestError(val error: Throwable): Result<Nothing>()