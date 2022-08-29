package com.tokopedia.manageaddress.ui.uimodel

sealed class FromFriendAddressListState<out T: Any?> {
    data class Success<out T: Any?>(val data:T) : FromFriendAddressListState<T>()
    data class Fail(val throwable: Throwable?, val errorMessage: String): FromFriendAddressListState<Nothing>()
    data class Loading(val isShowLoading: Boolean): FromFriendAddressListState<Nothing>()
}