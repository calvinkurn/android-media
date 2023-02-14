package com.tokopedia.mvc.presentation.creation.step3.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.*

data class VoucherCreationStepThreeUiState(
    val isLoading: Boolean = true,
    val originalPageMode: PageMode = PageMode.CREATE,
    val pageMode: PageMode = PageMode.CREATE,
    val voucherConfiguration: VoucherConfiguration = VoucherConfiguration(),
    val availableTargetBuyer: List<VoucherTargetBuyer> = listOf(VoucherTargetBuyer.ALL_BUYER, VoucherTargetBuyer.NEW_FOLLOWER),
    val spendingEstimation: Long = 0,
    val isNominalError: Boolean = false,
    val nominalErrorMsg: String = "",
    val isPercentageError: Boolean = false,
    val percentageErrorMsg: String = "",
    val isMaxDeductionError: Boolean = false,
    val maxDeductionErrorMsg: String = "",
    val isMinimumBuyError: Boolean = false,
    val minimumBuyErrorMsg: String = "",
    val isQuotaError: Boolean = false,
    val quotaErrorMsg: String = "",
    val fieldValidated: VoucherCreationStepThreeFieldValidation = VoucherCreationStepThreeFieldValidation.NONE,
    val isDiscountPromoTypeEnabled: Boolean = false
) {
    fun isInputValid(): Boolean {
        return if (voucherConfiguration.promoType == PromoType.FREE_SHIPPING) {
            validateWhenPromoTypeIsFreeShipping()
        } else {
            validateWhenPromoTypeIsCashbackOrDiscount()
        }
    }

    private fun validateWhenPromoTypeIsFreeShipping(): Boolean {
        return !isNominalError && !isMinimumBuyError && !isQuotaError
    }

    private fun validateWhenPromoTypeIsCashbackOrDiscount(): Boolean {
        return if (voucherConfiguration.benefitType == BenefitType.PERCENTAGE) {
            validateWhenBenefitIsPercentage()
        } else {
            validateWhenBenefitIsNominal()
        }
    }

    private fun validateWhenBenefitIsPercentage(): Boolean {
        return !isPercentageError && !isMaxDeductionError && !isMinimumBuyError && !isQuotaError
    }

    private fun validateWhenBenefitIsNominal(): Boolean {
        return !isNominalError && !isMinimumBuyError && !isQuotaError
    }
}
