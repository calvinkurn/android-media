package com.tokopedia.logisticseller.ui.reschedulepickup.uimodel

import com.tokopedia.logisticseller.data.model.RescheduleDayOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleReasonOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleTimeOptionModel

sealed class ReschedulePickupUiEvent {

    object SaveReschedule : ReschedulePickupUiEvent()
    data class LoadRescheduleInfo(val orderId: String) : ReschedulePickupUiEvent()
    data class SelectDay(val selectedDay: RescheduleDayOptionModel) : ReschedulePickupUiEvent()
    data class SelectTime(val selectedTime: RescheduleTimeOptionModel) : ReschedulePickupUiEvent()
    data class SelectReason(val selectedReason: RescheduleReasonOptionModel) :
        ReschedulePickupUiEvent()

    data class CustomReason(val reason: String) : ReschedulePickupUiEvent()

    data class ClickSubtitle(val url: String) : ReschedulePickupUiEvent()
    data class CloseDialog(val success: Boolean) : ReschedulePickupUiEvent()
    object PressBack : ReschedulePickupUiEvent()
}
