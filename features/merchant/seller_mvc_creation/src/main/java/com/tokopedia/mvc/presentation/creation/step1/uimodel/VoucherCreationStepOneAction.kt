package com.tokopedia.mvc.presentation.creation.step1.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration

sealed class VoucherCreationStepOneAction{
    data class ShowIneligibleState(val isVoucherProduct: Boolean): VoucherCreationStepOneAction()
    data class ContinueToNextStep(val voucherConfiguration: VoucherConfiguration): VoucherCreationStepOneAction()
    data class ShowError(val error: Throwable) : VoucherCreationStepOneAction()
}
