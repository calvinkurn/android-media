package com.tokopedia.mvc.presentation.creation.step2.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import java.util.*

sealed class VoucherCreationStepTwoEvent {
    data class InitVoucherConfiguration(
        val pageMode: PageMode,
        val voucherConfiguration: VoucherConfiguration
    ) :
        VoucherCreationStepTwoEvent()

    object TapBackButton : VoucherCreationStepTwoEvent()
    data class ChooseVoucherTarget(
        val isPublic: Boolean,
        val isChangingTargetBuyer: Boolean = false
    ) : VoucherCreationStepTwoEvent()

    data class OnVoucherNameChanged(val voucherName: String) : VoucherCreationStepTwoEvent()
    data class OnVoucherCodeChanged(val voucherCode: String) : VoucherCreationStepTwoEvent()
    data class OnVoucherRecurringToggled(val isActive: Boolean) : VoucherCreationStepTwoEvent()
    data class OnVoucherStartDateChanged(val calendar: Calendar) : VoucherCreationStepTwoEvent()
    data class OnVoucherEndDateChanged(val calendar: Calendar) : VoucherCreationStepTwoEvent()
    data class OnVoucherRecurringPeriodSelected(val selectedRecurringPeriod: Int) :
        VoucherCreationStepTwoEvent()

    data class NavigateToNextStep(val voucherConfiguration: VoucherConfiguration) :
        VoucherCreationStepTwoEvent()

    object HandleCoachMark : VoucherCreationStepTwoEvent()
}
