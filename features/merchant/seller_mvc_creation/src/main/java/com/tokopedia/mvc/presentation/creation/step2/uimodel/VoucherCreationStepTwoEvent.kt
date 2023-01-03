package com.tokopedia.mvc.presentation.creation.step2.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import java.util.*

sealed class VoucherCreationStepTwoEvent {
    data class InitVoucherConfiguration(val voucherConfiguration: VoucherConfiguration) :
        VoucherCreationStepTwoEvent()
    object TapBackButton : VoucherCreationStepTwoEvent()
    data class ChooseVoucherTarget(val isPublic: Boolean) : VoucherCreationStepTwoEvent()
    data class OnVoucherNameChanged(val voucherName: String) : VoucherCreationStepTwoEvent()
    data class OnVoucherCodeChanged(val voucherCode: String) : VoucherCreationStepTwoEvent()
    data class OnVoucherStartDateChanged(val calendar: Calendar) : VoucherCreationStepTwoEvent()
    data class OnVoucherEndDateChanged(val calendar: Calendar) : VoucherCreationStepTwoEvent()
    data class ValidateVoucherInput(val voucherConfiguration: VoucherConfiguration) : VoucherCreationStepTwoEvent()
    object NavigateToNextStep : VoucherCreationStepTwoEvent()
}
