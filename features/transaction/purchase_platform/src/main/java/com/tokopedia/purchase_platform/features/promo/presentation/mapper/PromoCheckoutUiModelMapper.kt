package com.tokopedia.purchase_platform.features.promo.presentation.mapper

import com.tokopedia.purchase_platform.common.feature.tokopointstnc.TokoPointsTncUiModel
import com.tokopedia.purchase_platform.features.promo.data.response.*
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*
import javax.inject.Inject

class PromoCheckoutUiModelMapper @Inject constructor() {

    fun mapFragmentUiModel(pageSource: Int): FragmentUiModel {
        return FragmentUiModel(
                uiData = FragmentUiModel.UiData().apply {
                    pageSource = pageSource
                    totalBenefit = 0
                    usedPromoCount = 0
                },
                uiState = FragmentUiModel.UiState().apply {
                    hasPreAppliedPromo = false
                    hasAnyPromoSelected = false
                    hasFailedToLoad = false
                }
        )
    }

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
                    promoCodes = couponListRecommendation.data.promoRecommendation.codes
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
                    tmpPromo = emptyList()
                },
                uiState = PromoEligibilityHeaderUiModel.UiState().apply {
                    isEnabled = couponSectionItem.isEnabled
                    if (couponSectionItem.isEnabled) {
                        isExpanded = !couponSectionItem.isCollapsed
                    } else {
                        isExpanded = false
                    }
                }
        )
    }

    fun mapPromoListHeaderUiModel(couponSubSection: SubSection, headerIdentifierId: Int, isHeaderEnabled: Boolean): PromoListHeaderUiModel {
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
                    var tmpHasSellectedPromoItem = false
                    couponSubSection.coupons.forEach {
                        if (it.isSelected) {
                            tmpHasSellectedPromoItem = true
                            return@forEach
                        }
                    }
                    hasSelectedPromoItem = tmpHasSellectedPromoItem
                    if (isHeaderEnabled) {
                        isExpanded = !couponSubSection.isCollapsed
                    } else {
                        isExpanded = true
                    }
                }
        )
    }

    fun mapPromoListItemUiModel(couponItem: Coupon, headerIdentifierId: Int, parentEnabled: Boolean, selectedPromo: List<String>): PromoListItemUiModel {
        return PromoListItemUiModel(
                uiData = PromoListItemUiModel.UiData().apply {
                    uniqueId = couponItem.uniqueId
                    title = couponItem.title
                    subTitle = couponItem.expiryInfo
                    benefitAmount = couponItem.benefitAmount
                    imageResourceUrls = couponItem.tagImageUrls
                    parentIdentifierId = headerIdentifierId
                    promoCode = couponItem.code
                    val clashingInfoMap = HashMap<String, String>()
                    couponItem.clashingInfos.forEach {
                        clashingInfoMap[it.code] = it.message
                    }
                    clashingInfo = clashingInfoMap
                    val tmpCurrentClashingPromoList = ArrayList<String>()
                    val tmpErrorMessage = StringBuilder()
                    selectedPromo.forEach {
                        if (clashingInfo.containsKey(it)) {
                            tmpCurrentClashingPromoList.add(it)
                            if (tmpErrorMessage.isNotBlank()) {
                                tmpErrorMessage.append("\n")
                            }
                            tmpErrorMessage.append(clashingInfo[it])
                        }
                    }
                    currentClashingPromo = tmpCurrentClashingPromoList

                    if (tmpErrorMessage.isEmpty()) {
                        tmpErrorMessage.append(couponItem.message)
                    }
                    errorMessage = if (couponItem.radioCheckState == PromoListItemUiModel.UiState.STATE_IS_DISABLED) tmpErrorMessage.toString() else ""
                },
                uiState = PromoListItemUiModel.UiState().apply {
                    isParentEnabled = parentEnabled
                    isSelected = couponItem.isSelected
                    isAttempted = couponItem.isAttempted
                    isAlreadyApplied = couponItem.isSelected
                }
        )
    }

    fun mapEmptyState(couponListRecommendation: CouponListRecommendation): PromoEmptyStateUiModel {
        return PromoEmptyStateUiModel(
                uiData = PromoEmptyStateUiModel.UiData().apply {
                    title = couponListRecommendation.data.emptyState.title
                    description = couponListRecommendation.data.emptyState.description
                    imageUrl = couponListRecommendation.data.emptyState.imageUrl
                },
                uiState = PromoEmptyStateUiModel.UiState().apply {

                }
        )
    }

    fun mapTokoPointsTncDetails(tncDetail: List<TncDetail>): ArrayList<TokoPointsTncUiModel> {
        val tokoPointsTncDetails = ArrayList<TokoPointsTncUiModel>()
        tncDetail.forEach {
            tokoPointsTncDetails.add(TokoPointsTncUiModel(imageUrl = it.iconImageUrl, description = it.description))
        }
        return tokoPointsTncDetails
    }
}