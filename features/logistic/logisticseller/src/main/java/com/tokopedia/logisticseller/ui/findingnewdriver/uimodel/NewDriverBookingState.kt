package com.tokopedia.logisticseller.ui.findingnewdriver.uimodel

sealed class NewDriverBookingState {
    data class Success(val message: String) : NewDriverBookingState()
    data class Fail(val errorMessage: String?) : NewDriverBookingState()
    data class Loading(val isShowLoading: Boolean) : NewDriverBookingState()
}
