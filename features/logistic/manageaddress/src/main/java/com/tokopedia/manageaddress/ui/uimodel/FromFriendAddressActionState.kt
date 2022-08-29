package com.tokopedia.manageaddress.ui.uimodel

sealed class FromFriendAddressActionState {
    object Success: FromFriendAddressActionState()
    data class Fail(val throwable: Throwable?, val errorMessage: String): FromFriendAddressActionState()
    data class Loading(val isShowLoading: Boolean): FromFriendAddressActionState()
}