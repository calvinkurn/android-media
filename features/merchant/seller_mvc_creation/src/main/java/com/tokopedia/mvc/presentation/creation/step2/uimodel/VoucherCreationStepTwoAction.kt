package com.tokopedia.mvc.presentation.creation.step2.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration

sealed class VoucherCreationStepTwoAction {
    data class BackToPreviousStep(val voucherConfiguration: VoucherConfiguration) : VoucherCreationStepTwoAction()
    data class ContinueToNextStep(val voucherConfiguration: VoucherConfiguration) : VoucherCreationStepTwoAction()
    object ShowCoachmark : VoucherCreationStepTwoAction()
    data class ShowError(val error: Throwable) : VoucherCreationStepTwoAction()
}
