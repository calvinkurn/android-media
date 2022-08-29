package com.tokopedia.manageaddress.ui.uimodel

sealed class ValidateShareAddressState {
    object Success: ValidateShareAddressState()
    object Fail: ValidateShareAddressState()
    data class Loading(val isShowLoading: Boolean): ValidateShareAddressState()
}