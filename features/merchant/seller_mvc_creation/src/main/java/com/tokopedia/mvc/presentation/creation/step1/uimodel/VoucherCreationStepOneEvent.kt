package com.tokopedia.mvc.presentation.creation.step1.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode

sealed class VoucherCreationStepOneEvent {
    data class InitVoucherConfiguration(
        val pageMode: PageMode,
        val voucherConfiguration: VoucherConfiguration
    ) : VoucherCreationStepOneEvent()

    data class ChooseVoucherType(
        val pageMode: PageMode,
        val isVoucherProduct: Boolean
    ) : VoucherCreationStepOneEvent()

    object resetFillState : VoucherCreationStepOneEvent()
    object HandleCoachmark : VoucherCreationStepOneEvent()
    object NavigateToNextStep : VoucherCreationStepOneEvent()
}
