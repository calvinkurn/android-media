package com.tokopedia.logisticCommon.domain.model

sealed class ShareAddressBottomSheetState<out T: Any> {
    data class Success<out T: Any>(val data:T) : ShareAddressBottomSheetState<T>()
    data class Fail(val errorMessage: String): ShareAddressBottomSheetState<Nothing>()
    data class Loading(val isShowLoading: Boolean): ShareAddressBottomSheetState<Nothing>()
}