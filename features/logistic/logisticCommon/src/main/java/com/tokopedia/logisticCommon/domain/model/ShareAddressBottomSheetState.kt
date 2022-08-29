package com.tokopedia.logisticCommon.domain.model

sealed class ShareAddressBottomSheetState {
    object Success: ShareAddressBottomSheetState()
    data class Fail(val errorMessage: String): ShareAddressBottomSheetState()
    data class Loading(val isShowLoading: Boolean): ShareAddressBottomSheetState()
}