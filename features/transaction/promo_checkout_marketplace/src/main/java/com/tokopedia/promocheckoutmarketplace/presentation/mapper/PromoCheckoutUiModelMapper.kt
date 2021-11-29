package com.tokopedia.promocheckoutmarketplace.presentation.mapper

import com.tokopedia.promocheckoutmarketplace.data.response.*
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.*
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import javax.inject.Inject

class PromoCheckoutUiModelMapper @Inject constructor() {

    fun mapFragmentUiModel(valuePageSource: Int): FragmentUiModel {
        return FragmentUiModel(
                uiData = FragmentUiModel.UiData().apply {
                    pageSource = valuePageSource
                    totalBenefit = 0
                    usedPromoCount = 0
                },
                uiState = FragmentUiModel.UiState().apply {
                    isLoading = true
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
                    isInitialization = true
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
                    tabId = couponSectionItem.id
                },
                uiState = PromoEligibilityHeaderUiModel.UiState().apply {
                    isEnabled = couponSectionItem.isEnabled
                    isCollapsed = couponSectionItem.isCollapsed
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
                    tabId = couponSubSection.id
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
                    isCollapsed = couponSubSection.isCollapsed
                }
        )
    }

    fun mapPromoListItemUiModel(couponItem: Coupon,
                                couponSubSection: SubSection,
                                headerIdentifierId: Int,
                                selectedPromo: List<String>,
                                index: Int = 0): PromoListItemUiModel {
        return PromoListItemUiModel(
                id = index.toString(),
                uiData = PromoListItemUiModel.UiData().apply {
                    uniqueId = couponItem.uniqueId
                    shopId = couponItem.shopId.toInt()
                    title = couponItem.title
                    expiryInfo = couponItem.expiryInfo
                    benefitAmount = couponItem.benefitAmount
                    imageResourceUrls = couponItem.tagImageUrls
                    parentIdentifierId = headerIdentifierId
                    promoCode = couponItem.code
                    couponAppLink = couponItem.couponAppLink
                    currencyDetailStr = couponItem.currencyDetailStr
                    coachMark = couponItem.coachMark
                    val clashingInfoMap = HashMap<String, String>()
                    if (couponItem.clashingInfos.isNotEmpty()) {
                        couponItem.clashingInfos.forEach {
                            clashingInfoMap[it.code] = couponItem.clashingInfos[0].message
                        }
                    }
                    clashingInfo = clashingInfoMap
                    val tmpCurrentClashingPromoList = ArrayList<String>()
                    val tmpErrorMessage = StringBuilder()
                    selectedPromo.forEach {
                        if (clashingInfo.containsKey(it)) {
                            tmpCurrentClashingPromoList.add(it)
                            tmpErrorMessage.clear()
                            tmpErrorMessage.append(clashingInfo[it])
                        }
                    }
                    currentClashingPromo = tmpCurrentClashingPromoList

                    if (tmpErrorMessage.isEmpty()) {
                        tmpErrorMessage.append(couponItem.message)
                    }
                    errorMessage = if (tmpErrorMessage.isNotBlank()) tmpErrorMessage.toString() else ""
                    promoInfos = couponItem.promoInfos
                    remainingPromoCount = couponSubSection.coupons.filter {
                        it.groupId == couponItem.groupId
                    }.size
                },
                uiState = PromoListItemUiModel.UiState().apply {
                    isParentEnabled = couponSubSection.isEnabled
                    isSelected = couponItem.isSelected
                    isAttempted = couponItem.isAttempted
                    isCausingOtherPromoClash = false
                    isHighlighted = couponItem.isHighlighted
                }
        )
    }

    fun mapErrorState(errorPage: ErrorPage): PromoErrorStateUiModel {
        return PromoErrorStateUiModel(
                uiData = PromoErrorStateUiModel.UiData().apply {
                    imageUrl = errorPage.img
                    title = errorPage.title
                    description = errorPage.desc
                    buttonText = errorPage.button.text
                    buttonDestination = errorPage.button.destination
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

    fun mapClearPromoResponse(response: ClearPromoResponse): ClearPromoUiModel {
        return ClearPromoUiModel().apply {
            successDataModel = SuccessDataUiModel().apply {
                success = response.successData.success
                tickerMessage = response.successData.tickerMessage
                defaultEmptyPromoMessage = response.successData.defaultEmptyPromoMessage
            }
        }
    }

    fun mapPromoLastSeenResponse(response: GetPromoSuggestionResponse): PromoLastSeenUiModel {
        return PromoLastSeenUiModel(
                uiData = PromoLastSeenUiModel.UiData().apply {
                    promoLastSeenItemUiModelList = ArrayList()
                    response.promoSuggestion.promoHistory.forEach {
                        val promoLastSeenItemUiModel = PromoLastSeenItemUiModel(
                                uiData = PromoLastSeenItemUiModel.UiData().apply {
                                    promoCode = it.promoCode
                                    promoTitle = it.promoContent.promoTitle
                                }
                        )
                        (promoLastSeenItemUiModelList as ArrayList<PromoLastSeenItemUiModel>).add(promoLastSeenItemUiModel)
                    }
                }
        )
    }

    fun mapPromoTabsUiModel(sectionTabs: List<SectionTab>): PromoTabUiModel {
        return PromoTabUiModel(
                uiData = PromoTabUiModel.UiData().apply {
                    tabs = sectionTabs
                },
                uiState = PromoTabUiModel.UiState().apply {
                    isInitialization = true
                    selectedTabPosition = 0
                }
        )
    }
}