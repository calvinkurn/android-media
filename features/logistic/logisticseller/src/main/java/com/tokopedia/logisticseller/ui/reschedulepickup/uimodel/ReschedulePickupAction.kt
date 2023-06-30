package com.tokopedia.logisticseller.ui.reschedulepickup.uimodel

sealed class ReschedulePickupAction {
    data class OpenTnCWebView(val url: String) : ReschedulePickupAction()
    data class ClosePage(val success: Boolean) : ReschedulePickupAction()
    data class ShowError(val error: ReschedulePickupErrorState) : ReschedulePickupAction()
}
