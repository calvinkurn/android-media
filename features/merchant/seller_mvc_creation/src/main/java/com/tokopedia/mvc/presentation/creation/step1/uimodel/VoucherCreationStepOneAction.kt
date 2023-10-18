package com.tokopedia.mvc.presentation.creation.step1.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode

sealed class VoucherCreationStepOneAction {
    data class ShowIneligibleState(val isVoucherProduct: Boolean, val message: String) : VoucherCreationStepOneAction()
    object ShowCoachmark : VoucherCreationStepOneAction()
    data class NavigateToNextStep(val pageMode: PageMode, val voucherConfiguration: VoucherConfiguration) : VoucherCreationStepOneAction()
    data class ShowError(val error: Throwable) : VoucherCreationStepOneAction()
}
