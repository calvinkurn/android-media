package com.tokopedia.purchase_platform.features.promo.presentation.mapper

import com.tokopedia.purchase_platform.features.promo.data.response.Coupon
import com.tokopedia.purchase_platform.features.promo.data.response.CouponListRecommendation
import com.tokopedia.purchase_platform.features.promo.data.response.CouponSection
import com.tokopedia.purchase_platform.features.promo.data.response.SubSection
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
                    promoCode = ""
                },
                uiState = PromoInputUiModel.UiState().apply {
                    isButtonSelectEnabled = false
                    isError = false
                }
        )
    }

    fun mapPromoEligibilityHeaderUiModel(couponSectionItem: CouponSection): PromoEligibilityHeaderUiModel {
        return PromoEligibilityHeaderUiModel(
                uiData = PromoEligibilityHeaderUiModel.UiData().apply {
                    title = couponSectionItem.title
                    subTitle = couponSectionItem.subTitle
                    tmpPromo = emptyList() // Todo : baca dari backend
                },
                uiState = PromoEligibilityHeaderUiModel.UiState().apply {
                    isEnabled = couponSectionItem.isEnabled
                }
        )
    }

    fun mapPromoListHeaderUiModel(couponSubSection: SubSection, headerIdentifierId: Int): PromoListHeaderUiModel {
        return PromoListHeaderUiModel(
                uiData = PromoListHeaderUiModel.UiData().apply {
                    title = couponSubSection.title
                    subTitle = couponSubSection.subTitle
                    iconUrl = couponSubSection.iconUrl
                    identifierId = headerIdentifierId
                    tmpPromoItemList = emptyList()
                },
                uiState = PromoListHeaderUiModel.UiState().apply {
                    isEnabled = couponSubSection.isEnabled
                    isCollapsed = false // Todo : baca dari backend
                }
        )
    }

    fun mapPromoListItemUiModel(couponItem: Coupon, headerIdentifierId: Int, selectedPromo: List<String>): PromoListItemUiModel {
        return PromoListItemUiModel(
                uiData = PromoListItemUiModel.UiData().apply {
                    title = couponItem.title
                    subTitle = couponItem.expiryInfo
                    val tmpErrorMessage = StringBuilder()
                    if (couponItem.clashingInfos.isNotEmpty()) {
                        for ((index, data) in couponItem.clashingInfos.withIndex()) {
                            tmpErrorMessage.append(data.message)
                            if (index < couponItem.clashingInfos.size - 1) {
                                tmpErrorMessage.append("\n")
                            }
                        }
                    } else {
                        tmpErrorMessage.append(couponItem.message)
                    }
                    errorMessage = if (couponItem.radioCheckState == PromoListItemUiModel.UiState.STATE_IS_DISABLED) tmpErrorMessage.toString() else ""
                    imageResourceUrls = couponItem.tagImageUrls
                    parentIdentifierId = headerIdentifierId
                    promoCode = couponItem.code
                    val clashingInfoMap = HashMap<String, String>()
                    couponItem.clashingInfos.forEach {
                        clashingInfoMap[it.code] = it.message
                    }
                    clashingInfo = clashingInfoMap
                    val tmpCurrentClashingPromoList = ArrayList<String>()
                    selectedPromo.forEach {
                        if (clashingInfo.containsKey(it)) {
                            tmpCurrentClashingPromoList.add(it)
                        }
                    }
                    currentClashingPromo = tmpCurrentClashingPromoList
                },
                uiState = PromoListItemUiModel.UiState().apply {
                    isEnabled = couponItem.radioCheckState == PromoListItemUiModel.UiState.STATE_IS_ENABLED
                    isSellected = couponItem.isSelected
                    isAttempted = couponItem.isAttempted
                }
        )
    }
}