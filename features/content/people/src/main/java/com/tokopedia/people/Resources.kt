package com.tokopedia.people

sealed class Resources<T>
class Loading<T> : Resources<T>()
data class Success<T>(val data: T, val nextCursor: String = "") : Resources<T>()
data class ErrorMessage<T>(val data: String) : Resources<T>()
