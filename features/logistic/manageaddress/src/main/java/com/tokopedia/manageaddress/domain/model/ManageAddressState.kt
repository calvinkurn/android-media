package com.tokopedia.manageaddress.domain.model

sealed class ManageAddressState<out T: Any> {
    data class Success<out T: Any>(val data:T) : ManageAddressState<T>()
    data class Fail(val throwable: Throwable?, val errorMessage: String): ManageAddressState<Nothing>()
    object Loading: ManageAddressState<Nothing>()
}