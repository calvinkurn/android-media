package com.tokopedia.manageaddress.ui.uimodel

sealed class FromFriendAddressActionState {
    data class Success(val message: String) : FromFriendAddressActionState()
    data class Fail(val errorMessage: String) : FromFriendAddressActionState()
    data class Loading(val isShowLoading: Boolean) : FromFriendAddressActionState()
}
