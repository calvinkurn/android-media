package com.tokopedia.logisticseller.ui.reschedulepickup.uimodel

import com.tokopedia.logisticseller.data.model.RescheduleDayOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleReasonOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleTimeOptionModel
import com.tokopedia.logisticseller.data.model.SaveRescheduleModel

data class ReschedulePickupState(
    val info: ReschedulePickupInfo = ReschedulePickupInfo(),
    val options: ReschedulePickupOptions = ReschedulePickupOptions(),
    val error: String = "",
    val valid: Boolean = false,
    val isCustomReason: Boolean = false,
    val reason: String = "",
    val customReasonError: String? = null,
    val saveRescheduleModel: SaveRescheduleModel? = null
)

enum class RescheduleBottomSheetState {
    DAY, TIME, REASON, NONE
}

data class ReschedulePickupInput(
    val day: String = "",
    val time: String = "",
    val reason: String = ""
)

data class ReschedulePickupOptions(
    val dayOptions: List<RescheduleDayOptionModel> = listOf(),
    val timeOptions: List<RescheduleTimeOptionModel> = listOf(),
    val reasonOptions: List<RescheduleReasonOptionModel> = listOf()
)

data class ReschedulePickupInfo(
    val invoice: String = "",
    val courier: String = "",
    val guide: String = "",
    val applink: String = "",
    val summary: String = ""
)

data class ReschedulePickupErrorState(
    val message: String = "",
    val action: RescheduleErrorAction
)

enum class RescheduleErrorAction {
    SHOW_TOASTER_FAILED_GET_RESCHEDULE, SHOW_TOASTER_FAILED_SAVE_RESCHEDULE, SHOW_EMPTY_STATE
}
