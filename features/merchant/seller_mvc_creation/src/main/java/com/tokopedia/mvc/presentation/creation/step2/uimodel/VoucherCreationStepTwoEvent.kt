package com.tokopedia.mvc.presentation.creation.step2.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration

sealed class VoucherCreationStepTwoEvent {
    data class InitVoucherConfiguration(val voucherConfiguration: VoucherConfiguration) :
        VoucherCreationStepTwoEvent()
    object TapBackButton : VoucherCreationStepTwoEvent()
    data class ChooseVoucherTarget(val isPublic: Boolean) : VoucherCreationStepTwoEvent()
}
