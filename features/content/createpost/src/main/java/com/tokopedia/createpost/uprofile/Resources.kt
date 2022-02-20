package com.tokopedia.createpost.uprofile

sealed class Resources<T>
class Loading<T> : Resources<T>()
data class Success<T>(val data : T) : Resources<T>()
data class ErrorMessage<T>(val data : String) : Resources<T>()