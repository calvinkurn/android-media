package com.tokopedia.manageaddress.ui.uimodel

sealed class ValidateShareAddressState {
    data class Success(val receiverUserName: String? = null) : ValidateShareAddressState()
    object Fail : ValidateShareAddressState()
    data class Loading(val isShowLoading: Boolean) : ValidateShareAddressState()
}
