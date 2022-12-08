package com.tokopedia.logisticseller.ui.findingnewdriver.uimodel

sealed class NewDriverBookingState {
    data class Success(val message: String) : NewDriverBookingState()
    object Fail : NewDriverBookingState()
    data class Loading(val isShowLoading: Boolean) : NewDriverBookingState()
}
