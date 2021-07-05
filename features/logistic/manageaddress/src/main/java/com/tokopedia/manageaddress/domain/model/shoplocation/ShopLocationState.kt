package com.tokopedia.manageaddress.domain.model.shoplocation

sealed class ShopLocationState<out T: Any> {
    data class Success<out T: Any>(val data:T) : ShopLocationState<T>()
    data class Fail(val throwable: Throwable?, val errorMessage: String): ShopLocationState<Nothing>()
    object Loading: ShopLocationState<Nothing>()
}