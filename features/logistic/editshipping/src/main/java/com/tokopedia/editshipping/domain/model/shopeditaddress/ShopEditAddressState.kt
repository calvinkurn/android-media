package com.tokopedia.editshipping.domain.model.shopeditaddress

sealed class ShopEditAddressState<out T: Any> {
    data class Success<out T: Any>(val data:T) : ShopEditAddressState<T>()
    data class Fail(val throwable: Throwable?, val errorMessage: String): ShopEditAddressState<Nothing>()
    object Loading: ShopEditAddressState<Nothing>()
}