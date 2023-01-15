package com.tokopedia.mvc.presentation.creation.step3.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration

sealed class VoucherCreationStepThreeAction {
    data class BackToPreviousStep(val voucherConfiguration: VoucherConfiguration) : VoucherCreationStepThreeAction()
    data class ContinueToNextStep(val voucherConfiguration: VoucherConfiguration) : VoucherCreationStepThreeAction()
    object ShowCoachmark : VoucherCreationStepThreeAction()
    data class ShowError(val error: Throwable) : VoucherCreationStepThreeAction()
}
