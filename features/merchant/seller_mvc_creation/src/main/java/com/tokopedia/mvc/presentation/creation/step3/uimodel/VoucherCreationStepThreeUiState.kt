package com.tokopedia.mvc.presentation.creation.step3.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType

data class VoucherCreationStepThreeUiState(
    val isLoading: Boolean = true,
    val originalPageMode: PageMode = PageMode.CREATE,
    val pageMode: PageMode = PageMode.CREATE,
    val voucherConfiguration: VoucherConfiguration = VoucherConfiguration(),
    val isNominalError: Boolean = false,
    val nominalErrorMsg: String = "",
    val isPercentageError: Boolean = false,
    val percentageErrorMsg: String = "",
    val isMaxDeductionError: Boolean = false,
    val maxDeductionErrorMsg: String = "",
    val isMinimumBuyError: Boolean = false,
    val minimumBuyErrorMsg: String = "",
    val isQuotaError: Boolean = false,
    val quotaErrorMsg: String = ""
) {
    fun isValid(): Boolean {
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
