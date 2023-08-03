package com.tokopedia.promocheckoutmarketplace.presentation.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.promocheckoutmarketplace.data.response.BenefitDetail
import com.tokopedia.promocheckoutmarketplace.data.response.BottomSheet
import com.tokopedia.promocheckoutmarketplace.data.response.ClearPromoResponse
import com.tokopedia.promocheckoutmarketplace.data.response.Coupon
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendation
import com.tokopedia.promocheckoutmarketplace.data.response.CouponSection
import com.tokopedia.promocheckoutmarketplace.data.response.ErrorPage
import com.tokopedia.promocheckoutmarketplace.data.response.GetPromoSuggestionResponse
import com.tokopedia.promocheckoutmarketplace.data.response.PromoInfo
import com.tokopedia.promocheckoutmarketplace.data.response.SectionTab
import com.tokopedia.promocheckoutmarketplace.data.response.SubSection
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.BoInfoBottomSheetUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.FragmentUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoEligibilityHeaderUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoEmptyStateUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoErrorStateUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoInputUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListHeaderUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoRecommendationUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoSuggestionItemUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoSuggestionUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoTabUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import javax.inject.Inject

class PromoCheckoutUiModelMapper @Inject constructor() {

    fun mapFragmentUiModel(valuePageSource: Int, defaultErrorMessage: String): FragmentUiModel {
        return FragmentUiModel(
            uiData = FragmentUiModel.UiData().apply {
                pageSource = valuePageSource
                totalBenefit = 0
                usedPromoCount = 0
                this.defaultErrorMessage = defaultErrorMessage
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
                                    it.secondaryCoupons.forEach {
                                        if (couponListRecommendation.data.promoRecommendation.codes.contains(it.code)) {
                                            totalBenefit += it.benefitAmount
                                        }
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
                if (!couponSectionItem.isEnabled) {
                    tabId = couponSectionItem.subSections.firstOrNull()?.id ?: ""
                }
            },
            uiState = PromoEligibilityHeaderUiModel.UiState().apply {
                isEnabled = couponSectionItem.isEnabled
            }
        )
    }

    fun mapPromoListHeaderUiModel(couponSubSection: SubSection, couponSection: CouponSection, headerIdentifierId: Int, isHeaderEnabled: Boolean): PromoListHeaderUiModel {
        return PromoListHeaderUiModel(
            uiData = PromoListHeaderUiModel.UiData().apply {
                title = couponSubSection.title
                subTitle = couponSubSection.subTitle
                iconUnify = couponSubSection.iconUnify
                iconUrl = couponSubSection.iconUrl
                identifierId = headerIdentifierId
                tabId = if (isHeaderEnabled) {
                    couponSubSection.id
                } else {
                    couponSection.id
                }
            },
            uiState = PromoListHeaderUiModel.UiState().apply {
                isEnabled = couponSubSection.isEnabled
                var tmpHasSelectedPromoItem = false
                couponSubSection.coupons.forEach {
                    if (it.isSelected) {
                        tmpHasSelectedPromoItem = true
                        return@forEach
                    }
                }
                hasSelectedPromoItem = tmpHasSelectedPromoItem
            }
        )
    }

    fun mapPromoListItemUiModel(
        couponItem: Coupon,
        couponSubSection: SubSection,
        couponSection: CouponSection,
        headerIdentifierId: Int,
        selectedPromo: List<String>,
        recommendedPromo: List<String>,
        index: Int = 0
    ): PromoListItemUiModel {
        val promoItem = PromoListItemUiModel(
            id = index.toString(),
            uiData = PromoListItemUiModel.UiData().apply {
                promoId = couponItem.promoId
                uniqueId = couponItem.uniqueId
                shopId = couponItem.shopId.toIntOrZero()
                title = couponItem.title
                benefitAmount = couponItem.benefitAmount
                parentIdentifierId = headerIdentifierId
                promoCode = couponItem.code
                couponAppLink = couponItem.couponAppLink
                currencyDetailStr = couponItem.currencyDetailStr
                coachMark = couponItem.coachMark
                clashingInfos = couponItem.clashingInfos
                boClashingInfos = couponItem.boClashingInfos
                boAdditionalData = couponItem.additionalBoData
                benefitAdjustmentMessage = couponItem.benefitAdjustmentMessage
                secondaryCoupons = couponItem.secondaryCoupons
                val tmpCurrentClashingPromoList = ArrayList<String>()
                var tmpClashingIconUrl = ""
                val tmpErrorMessage = StringBuilder()
                tmpClashingIconUrl = clashingInfos.firstOrNull()?.icon ?: ""
                selectedPromo.forEach { promoCode ->
                    val clashingInfo = clashingInfos.firstOrNull { clashingInfo -> clashingInfo.code == promoCode }
                    if (clashingInfo != null) {
                        tmpCurrentClashingPromoList.add(promoCode)
                        tmpErrorMessage.clear()
                        tmpErrorMessage.append(clashingInfo.message)
                        tmpClashingIconUrl = clashingInfo.icon
                    }
                }
                currentClashingPromo = tmpCurrentClashingPromoList
                val tmpCurrentClashingSecondaryPromoList = ArrayList<String>()
                var tmpClashingSecondaryIconUrl = ""
                val tmpErrorMessageSecondary = StringBuilder()
                if (secondaryCoupons.isNotEmpty()) {
                    selectedPromo.forEach { promoCode ->
                        val clashingInfo =
                            secondaryCoupons.first().clashingInfos.firstOrNull { clashingInfo -> clashingInfo.code == promoCode }
                        if (clashingInfo != null) {
                            tmpCurrentClashingSecondaryPromoList.add(promoCode)
                            tmpErrorMessageSecondary.clear()
                            tmpErrorMessageSecondary.append(clashingInfo.message)
                            tmpClashingSecondaryIconUrl = clashingInfo.icon
                        }
                    }
                }
                currentClashingSecondaryPromo = tmpCurrentClashingSecondaryPromoList

                if (hasClashingPromo) {
                    if (currentClashingSecondaryPromo.isNotEmpty()) {
                        if (tmpErrorMessageSecondary.isEmpty()) {
                            tmpErrorMessageSecondary.append(secondaryCoupons.first().message)
                        }
                        errorMessage = tmpErrorMessageSecondary.toString()
                        errorIcon = tmpClashingSecondaryIconUrl
                    } else if (currentClashingPromo.isNotEmpty()) {
                        if (tmpErrorMessage.isEmpty()) {
                            tmpErrorMessage.append(couponItem.message)
                        }
                        errorMessage = tmpErrorMessage.toString()
                        errorIcon = tmpClashingIconUrl
                    }
                }
                if (tmpErrorMessage.isEmpty()) {
                    tmpErrorMessage.append(couponItem.message)
                    errorMessage = tmpErrorMessage.toString()
                }
                promoInfos = couponItem.promoInfos
                remainingPromoCount = couponSubSection.couponGroups.firstOrNull {
                    it.id == couponItem.groupId
                }?.count ?: 0
                tabId = if (couponSubSection.isEnabled) {
                    couponSubSection.id
                } else {
                    couponSection.id
                }
                promoInfos.firstOrNull { it.validationType == PromoInfo.VALIDATION_TYPE_SHIPPING }?.let {
                    shippingOptions = it.methods.joinToString(",")
                }
                promoInfos.firstOrNull { it.validationType == PromoInfo.VALIDATION_TYPE_PAYMENT }?.let {
                    paymentOptions = it.methods.joinToString(",")
                }
                benefitDetail = couponItem.benefitDetail.firstOrNull() ?: BenefitDetail()
                cta = couponItem.cta
            },
            uiState = PromoListItemUiModel.UiState().apply {
                isParentEnabled = couponSubSection.isEnabled
                isPreSelected = couponItem.isSelected || couponItem.secondaryCoupons.any { it.isSelected }
                isSelected = couponItem.isSelected || couponItem.secondaryCoupons.any { it.isSelected }
                isAttempted = couponItem.isAttempted || couponItem.secondaryCoupons.any { it.isAttempted }
                isCausingOtherPromoClash = false
                isHighlighted = couponItem.isHighlighted || couponItem.secondaryCoupons.any { it.isHighlighted }
                val lastPromo = couponSubSection.coupons.lastOrNull()
                isLastPromoItem = lastPromo != null && (lastPromo.code == couponItem.code || lastPromo.groupId == couponItem.groupId)
                isBebasOngkir = couponItem.isBebasOngkir
                isContainActionableGopayCicilCTA =
                    couponItem.cta.applink.isNotEmpty() &&
                    couponItem.cta.text.isNotEmpty() &&
                    couponItem.cta.type == CTA_TYPE_REGISTER_GPL_CICIL
            }
        )
        promoItem.uiState.isDisabled = !promoItem.uiState.isParentEnabled || promoItem.uiData.errorMessage.isNotBlank()
        if (recommendedPromo.isNotEmpty()) {
            promoItem.uiState.isRecommended = recommendedPromo.contains(promoItem.uiData.promoCode) ||
                promoItem.uiData.secondaryCoupons.any { recommendedPromo.contains(it.code) }
        }
        return promoItem
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

    fun mapPromoSuggestionResponse(response: GetPromoSuggestionResponse): PromoSuggestionUiModel {
        return PromoSuggestionUiModel(
            uiData = PromoSuggestionUiModel.UiData().apply {
                promoSuggestionItemUiModelList = ArrayList()
                response.promoSuggestion.promoHistory.forEach {
                    val promoSuggestionItemUiModel = PromoSuggestionItemUiModel(
                        uiData = PromoSuggestionItemUiModel.UiData().apply {
                            promoCode = it.promoCode
                            promoTitle = it.promoContent.promoTitle
                        }
                    )
                    (promoSuggestionItemUiModelList as ArrayList<PromoSuggestionItemUiModel>).add(promoSuggestionItemUiModel)
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

    fun mapBoInfoBottomSheetUiModel(bottomSheet: BottomSheet): BoInfoBottomSheetUiModel {
        return BoInfoBottomSheetUiModel(
            uiData = BoInfoBottomSheetUiModel.UiData(
                title = bottomSheet.title,
                contentTitle = bottomSheet.contentTitle,
                contentDescription = bottomSheet.contentDescription,
                imageUrl = bottomSheet.imageUrl,
                buttonText = bottomSheet.buttonText
            ),
            uiState = BoInfoBottomSheetUiModel.UiState(
                isVisible = bottomSheet.title.isNotBlank() && bottomSheet.contentTitle.isNotBlank() &&
                    bottomSheet.contentDescription.isNotBlank() && bottomSheet.imageUrl.isNotBlank() &&
                    bottomSheet.buttonText.isNotBlank()
            )
        )
    }

    companion object {
        private const val CTA_TYPE_REGISTER_GPL_CICIL = "register_gpl_cicil"
    }
}
