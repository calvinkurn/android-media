package com.tokopedia.manageaddress.ui.uimodel

sealed class ValidateShareAddressState {
    data class Success(val receiverUserName: String? = null) : ValidateShareAddressState()
    data class Fail(val throwable: Throwable) : ValidateShareAddressState()
}
