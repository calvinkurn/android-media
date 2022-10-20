package com.tokopedia.manageaddress.domain.model.shareaddress

sealed class ShareAddressBottomSheetState {
    object Success: ShareAddressBottomSheetState()
    data class Fail(val errorMessage: String): ShareAddressBottomSheetState()
    data class Loading(val isShowLoading: Boolean): ShareAddressBottomSheetState()
}
