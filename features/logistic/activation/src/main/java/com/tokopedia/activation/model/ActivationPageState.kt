package com.tokopedia.activation.model

sealed class ActivationPageState<out T: Any> {
    data class Success<out T: Any>(val data:T) : ActivationPageState<T>()
    data class Fail(val throwable: Throwable?, val errorMessage: String): ActivationPageState<Nothing>()
    object Loading: ActivationPageState<Nothing>()
}