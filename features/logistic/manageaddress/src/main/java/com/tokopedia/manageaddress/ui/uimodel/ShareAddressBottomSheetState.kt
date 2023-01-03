package com.tokopedia.manageaddress.ui.uimodel

sealed class ShareAddressBottomSheetState {
    object Success : ShareAddressBottomSheetState()
    data class Fail(val errorMessage: String) : ShareAddressBottomSheetState()
    data class Loading(val isShowLoading: Boolean) : ShareAddressBottomSheetState()
}
