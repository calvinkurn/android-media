package com.tokopedia.manageaddress.domain.model.shareaddress

sealed class FromFriendAddressActionState {
    object Success: FromFriendAddressActionState()
    data class Fail(val throwable: Throwable?, val errorMessage: String): FromFriendAddressActionState()
    data class Loading(val isShowLoading: Boolean): FromFriendAddressActionState()
}