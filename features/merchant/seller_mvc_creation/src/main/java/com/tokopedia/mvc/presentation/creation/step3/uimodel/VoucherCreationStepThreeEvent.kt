package com.tokopedia.mvc.presentation.creation.step3.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer

sealed class VoucherCreationStepThreeEvent {
    data class InitVoucherConfiguration(
        val pageMode: PageMode,
        val voucherConfiguration: VoucherConfiguration
    ) :
        VoucherCreationStepThreeEvent()

    object TapBackButton : VoucherCreationStepThreeEvent()
    data class ChoosePromoType(val promoType: PromoType) : VoucherCreationStepThreeEvent()
    data class ChooseBenefitType(val benefitType: BenefitType) : VoucherCreationStepThreeEvent()
    data class OnInputNominalChanged(val nominal: Long) : VoucherCreationStepThreeEvent()
    data class OnInputPercentageChanged(val percentage: Long) : VoucherCreationStepThreeEvent()
    data class OnInputMaxDeductionChanged(val maxDeduction: Long) :
        VoucherCreationStepThreeEvent()

    data class OnInputMinimumBuyChanged(val minimumBuy: Long) : VoucherCreationStepThreeEvent()
    data class OnInputQuotaChanged(val quota: Long) : VoucherCreationStepThreeEvent()

    object ResetInput : VoucherCreationStepThreeEvent()
    data class ChooseTargetBuyer(val targetBuyer: VoucherTargetBuyer) :
        VoucherCreationStepThreeEvent()

    data class NavigateToNextStep(val voucherConfiguration: VoucherConfiguration) :
        VoucherCreationStepThreeEvent()

    object HandleCoachMark : VoucherCreationStepThreeEvent()
}
