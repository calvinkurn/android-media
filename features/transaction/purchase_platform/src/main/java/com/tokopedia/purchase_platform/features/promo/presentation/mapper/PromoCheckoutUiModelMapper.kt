package com.tokopedia.purchase_platform.features.promo.presentation.mapper

import com.tokopedia.purchase_platform.features.promo.data.response.CouponListRecommendation
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*
import javax.inject.Inject

class PromoCheckoutUiModelMapper @Inject constructor() {

    fun mapPromoRecommendationUiModel(couponListRecommendation: CouponListRecommendation): PromoRecommendationUiModel {
        return PromoRecommendationUiModel(
                uiData = PromoRecommendationUiModel.UiData().apply {
                    promoCount = couponListRecommendation.data.promoRecommendation.codes.size
                    var totalBenefit = 0
                    couponListRecommendation.data.couponSections.forEach {
                        if (it.isEnabled) {
                            it.subSections.forEach {
                                if (it.isEnabled) {
                                    it.coupons.forEach {
                                        if (couponListRecommendation.data.promoRecommendation.codes.contains(it.code)) {
                                            totalBenefit += it.benefitAmount
                                        }
                                    }
                                }
                            }
                        }
                    }
                    promoTotalBenefit = totalBenefit
                },
                uiState = PromoRecommendationUiModel.UiState().apply {
                    isButtonSelectEnabled = true
                }
        )
    }

    fun mapPromoInputUiModel(): PromoInputUiModel {
        return PromoInputUiModel(
                uiData = PromoInputUiModel.UiData().apply {

                },
                uiState = PromoInputUiModel.UiState().apply {

                }
        )
    }

    fun mapPromoEligibleHeaderUiModel(): PromoEligibilityHeaderUiModel {
        return PromoEligibilityHeaderUiModel(
                uiData = PromoEligibilityHeaderUiModel.UiData().apply {

                },
                uiState = PromoEligibilityHeaderUiModel.UiState().apply {

                }
        )
    }

    fun mapPromoListHeaderUiModel(): PromoListHeaderUiModel {
        return PromoListHeaderUiModel(
                uiData = PromoListHeaderUiModel.UiData().apply {

                },
                uiState = PromoListHeaderUiModel.UiState().apply {

                }
        )
    }

    fun mapPromoListItemUiModel(): PromoListItemUiModel {
        return PromoListItemUiModel(
                uiData = PromoListItemUiModel.UiData().apply {

                },
                uiState = PromoListItemUiModel.UiState().apply {

                }
        )
    }
}