package com.tokopedia.logisticseller.ui.returntoshipper.uimodel

sealed class ReturnToShipperState<out T : Any?> {
    data class ShowRtsConfirmDialog<out T : Any?>(val data: T) : ReturnToShipperState<T>()
    data class ShowToaster(val errorMessage: String) : ReturnToShipperState<Nothing>()
    data class ShowLoading(val isShowLoading: Boolean) : ReturnToShipperState<Nothing>()
    object ShowRtsSuccessDialog : ReturnToShipperState<Nothing>()
    object ShowRtsFailedDialog : ReturnToShipperState<Nothing>()
}
