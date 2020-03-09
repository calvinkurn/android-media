package com.tokopedia.purchase_platform.features.promo.presentation.mapper

import com.tokopedia.purchase_platform.features.promo.data.response.validate_use.AdditionalInfo
import com.tokopedia.purchase_platform.features.promo.data.response.validate_use.BenefitSummaryInfo
import com.tokopedia.purchase_platform.features.promo.data.response.validate_use.PromoValidateUseResponse
import com.tokopedia.purchase_platform.features.promo.data.response.validate_use.ValidateUsePromoRevamp
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.*

/**
 * Created by fwidjaja on 2020-03-05.
 */
class ValidateUsePromoCheckoutMapper {

    companion object {
        fun mapToValidateUseRevampPromoUiModel(validateUsePromoRevamp: ValidateUsePromoRevamp): ValidateUsePromoRevampUiModel {
            return ValidateUsePromoRevampUiModel(
                    status = validateUsePromoRevamp.status,
                    message = validateUsePromoRevamp.message,
                    promoUiModel = validateUsePromoRevamp.promo?.let { mapToPromoUiModel(it) }
            )
        }

        private fun mapToPromoUiModel(promo: PromoValidateUseResponse?): PromoUiModel {
            return PromoUiModel(
                additionalInfoUiModel = mapToAdditionalInfoUiModel(promo?.additionalInfo),
                    benefitSummaryInfoUiModel = mapToBenefitSummaryInfoUiModel(promo?.benefitSummaryInfo)
            )
        }

        private fun mapToAdditionalInfoUiModel(additionalInfo: AdditionalInfo?) : AdditionalInfoUiModel {
            val additionalInfoUiModel = AdditionalInfoUiModel()
            additionalInfo?.messageInfo?.let {
                additionalInfoUiModel.messageInfoUiModel.message = it.message.toString()
                additionalInfoUiModel.messageInfoUiModel.detail = it.detail.toString()
            }
            additionalInfo?.errorDetail?.let {
                additionalInfoUiModel.errorDetailUiModel.message = it.message.toString()
            }
            return additionalInfoUiModel
        }

        private fun mapToBenefitSummaryInfoUiModel(benefitSummaryInfo: BenefitSummaryInfo?): BenefitSummaryInfoUiModel {
            val benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel()
            benefitSummaryInfo?.let { benefit ->
                benefit.finalBenefitText?.let { benefitSummaryInfoUiModel.finalBenefitText = it }
                benefit.finalBenefitAmountStr?.let { benefitSummaryInfoUiModel.finalBenefitAmountStr = it }
            }
            return benefitSummaryInfoUiModel
        }
    }
}