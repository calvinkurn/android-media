package com.tokopedia.promousage.view.mapper

import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.promousage.data.response.BenefitDetail
import com.tokopedia.promousage.data.response.Coupon
import com.tokopedia.promousage.data.response.CouponCardDetail
import com.tokopedia.promousage.data.response.CouponSection
import com.tokopedia.promousage.data.response.GetPromoListRecommendationEntryPointResponse
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponse
import com.tokopedia.promousage.data.response.PromoRecommendation
import com.tokopedia.promousage.data.response.SecondaryCoupon
import com.tokopedia.promousage.domain.entity.BoAdditionalData
import com.tokopedia.promousage.domain.entity.PromoAttemptedError
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.promousage.domain.entity.PromoItemBenefitDetail
import com.tokopedia.promousage.domain.entity.PromoItemCardDetail
import com.tokopedia.promousage.domain.entity.PromoItemClashingInfo
import com.tokopedia.promousage.domain.entity.PromoItemCta
import com.tokopedia.promousage.domain.entity.PromoItemInfo
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.PromoPageSection
import com.tokopedia.promousage.domain.entity.PromoPageTickerInfo
import com.tokopedia.promousage.domain.entity.PromoSavingInfo
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoAttemptItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.domain.entity.list.SecondaryPromoItem
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import javax.inject.Inject

class PromoUsageGetPromoListRecommendationMapper @Inject constructor() {

    fun mapPromoListRecommendationEntryPointResponseToEntryPointInfo(
        response: GetPromoListRecommendationEntryPointResponse
    ): PromoEntryPointInfo {
        return PromoEntryPointInfo(
            messages = response.promoListRecommendation.data.entryPointInfo.messages,
            iconUrl = response.promoListRecommendation.data.entryPointInfo.iconUrl,
            color = response.promoListRecommendation.data.entryPointInfo.state,
            isClickable = response.promoListRecommendation.data.entryPointInfo.clickable,
            isSuccess = response.promoListRecommendation.data.resultStatus.success,
            statusCode = response.promoListRecommendation.data.resultStatus.code
        )
    }

    fun mapPromoListRecommendationResponseToPageTickerInfo(
        response: GetPromoListRecommendationResponse
    ): PromoPageTickerInfo {
        return PromoPageTickerInfo(
            message = response.promoListRecommendation.data.tickerInfo.message,
            iconUrl = response.promoListRecommendation.data.tickerInfo.iconUrl,
            backgroundUrl = response.promoListRecommendation.data.tickerInfo.backgroundUrl
        )
    }

    fun mapPromoListRecommendationResponseToSavingInfo(
        response: GetPromoListRecommendationResponse
    ): PromoSavingInfo {
        return PromoSavingInfo(
            message = response.promoListRecommendation.data.additionalMessage
        )
    }

    fun mapPromoListRecommendationResponseToPromoSections(
        response: GetPromoListRecommendationResponse
    ): List<DelegateAdapterItem> {
        val recommendedPromoCodes = response.promoListRecommendation.data.promoRecommendation.codes
        val selectedPromoCodes = response.promoListRecommendation.data.couponSections
            .flatMap { it.coupons }.filter { it.isSelected }.map { it.code }
        val selectedSecondaryPromoCodes = response.promoListRecommendation.data.couponSections
            .flatMap { it.coupons }.flatMap { it.secondaryCoupon }.filter { it.isSelected }.map { it.code }
        val allSelectedPromoCodes = selectedPromoCodes.plus(selectedSecondaryPromoCodes)

        var hasRecommendedOrOtherSection = false
        val items = mutableListOf<DelegateAdapterItem>()
        response.promoListRecommendation.data.couponSections.map { couponSection ->
            when (couponSection.id) {
                PromoPageSection.SECTION_RECOMMENDATION -> {
                    if (couponSection.coupons.isNotEmpty()) {
                        hasRecommendedOrOtherSection = true
                        items.add(
                            mapCouponSectionToPromoRecommendation(
                                promoRecommendation = response.promoListRecommendation.data.promoRecommendation,
                                couponSection = couponSection,
                                recommendedPromoCodes = recommendedPromoCodes,
                                selectedPromoCodes = allSelectedPromoCodes
                            )
                        )
                        couponSection.coupons.filter { it.isGroupHeader }
                            .forEachIndexed { index, coupon ->
                                items.add(
                                    mapCouponToPromo(
                                        index = index,
                                        couponSection = couponSection,
                                        coupon = coupon,
                                        recommendedPromoCodes = recommendedPromoCodes,
                                        selectedPromoCodes = selectedPromoCodes,
                                        selectedSecondaryPromoCodes = selectedSecondaryPromoCodes
                                    )
                                )
                            }
                    }
                }

                else -> {
                    if (couponSection.id != PromoPageSection.SECTION_INPUT_PROMO_CODE) {
                        if (couponSection.coupons.isNotEmpty()) {
                            val coupons = couponSection.coupons.filter { it.isGroupHeader }
                            hasRecommendedOrOtherSection = true

                            val selectedPromoInSection = couponSection.coupons
                                .filter { it.isSelected }
                            val selectedSecondaryPromoInSection = couponSection.coupons
                                .flatMap { it.secondaryCoupon }.filter { it.isSelected }

                            val isExpanded = !couponSection.isCollapsed
                            val hasSelectedPromoInSection = selectedPromoInSection.isNotEmpty() ||
                                selectedSecondaryPromoInSection.isNotEmpty()

                            val hiddenPromoCount = if (selectedPromoInSection.isNotEmpty()) {
                                couponSection.coupons.size - selectedPromoInSection.size
                            } else {
                                coupons.size - 1
                            }
                            items.add(
                                PromoAccordionHeaderItem(
                                    id = couponSection.id,
                                    title = couponSection.title,
                                    isExpanded = hiddenPromoCount > 0 && (isExpanded || hasSelectedPromoInSection),
                                    totalPromoCount = coupons.size
                                )
                            )
                            coupons.forEachIndexed { index, coupon ->
                                items.add(
                                    mapCouponToPromo(
                                        index = index,
                                        couponSection = couponSection,
                                        coupon = coupon,
                                        recommendedPromoCodes = recommendedPromoCodes,
                                        selectedPromoCodes = selectedPromoCodes,
                                        selectedSecondaryPromoCodes = selectedSecondaryPromoCodes
                                    )
                                )
                            }
                            if (hiddenPromoCount > 0 && (isExpanded || hasSelectedPromoInSection)) {
                                items.add(
                                    PromoAccordionViewAllItem(
                                        headerId = couponSection.id,
                                        hiddenPromoCount = hiddenPromoCount,
                                        totalPromoCount = coupons.size,
                                        isExpanded = true,
                                        isVisible = true
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        val attemptedPromoSection = response.promoListRecommendation.data.couponSections
            .firstOrNull { it.id == PromoPageSection.SECTION_INPUT_PROMO_CODE }
        val attemptedPromoError = response.promoListRecommendation.data.attemptedPromoCodeError
        items.add(
            PromoAttemptItem(
                id = PromoPageSection.SECTION_INPUT_PROMO_CODE,
                label = attemptedPromoSection?.title ?: "",
                errorAttemptedPromoCode = attemptedPromoError.code,
                errorMessage = attemptedPromoError.message,
                hasOtherSection = hasRecommendedOrOtherSection,
                hasAttemptedPromo = attemptedPromoSection?.coupons?.isNotEmpty() ?: false
            )
        )
        attemptedPromoSection?.coupons?.forEachIndexed { index, coupon ->
            items.add(
                mapCouponToPromo(
                    index = index,
                    couponSection = attemptedPromoSection,
                    coupon = coupon,
                    recommendedPromoCodes = recommendedPromoCodes,
                    selectedPromoCodes = selectedPromoCodes,
                    selectedSecondaryPromoCodes = selectedSecondaryPromoCodes
                )
            )
        }
        return items
    }

    private fun mapCouponSectionToPromoRecommendation(
        promoRecommendation: PromoRecommendation,
        couponSection: CouponSection,
        recommendedPromoCodes: List<String>,
        selectedPromoCodes: List<String>
    ): PromoRecommendationItem {
        return PromoRecommendationItem(
            id = PromoPageSection.SECTION_RECOMMENDATION,
            title = couponSection.title,
            message = promoRecommendation.message,
            messageSelected = promoRecommendation.messageSelected,
            selectedCodes = selectedPromoCodes,
            codes = recommendedPromoCodes,
            backgroundUrl = promoRecommendation.backgroundUrl,
            backgroundColor = promoRecommendation.backgroundColor,
            animationUrl = promoRecommendation.animationUrl
        )
    }

    private fun mapCouponToPromo(
        index: Int,
        couponSection: CouponSection,
        coupon: Coupon,
        recommendedPromoCodes: List<String>,
        selectedPromoCodes: List<String>,
        selectedSecondaryPromoCodes: List<String>
    ): PromoItem {
        val secondaryCoupon: SecondaryCoupon? = coupon.secondaryCoupon.firstOrNull()
        val remainingPromoCount = couponSection.couponGroups
            .firstOrNull { it.id == coupon.groupId }?.count ?: 1

        val isRecommended = recommendedPromoCodes.isNotEmpty() &&
            (
                recommendedPromoCodes.contains(coupon.code) ||
                    recommendedPromoCodes.contains(secondaryCoupon?.code)
                )
        val isSelected = coupon.isSelected || secondaryCoupon?.isSelected ?: false

        val primaryClashingInfos =
            coupon.clashingInfos
                .filter {
                    coupon.code != it.code && secondaryCoupon?.code != it.code &&
                        (selectedPromoCodes.contains(it.code) || selectedSecondaryPromoCodes.contains(it.code))
                }
        val secondaryClashingInfos =
            secondaryCoupon?.clashingInfos
                ?.filter {
                    if (isRecommended) {
                        coupon.code != it.code && secondaryCoupon.code != it.code &&
                            (selectedPromoCodes.contains(it.code) || selectedSecondaryPromoCodes.contains(it.code)) &&
                            !recommendedPromoCodes.contains(it.code)
                    } else {
                        coupon.code != it.code && secondaryCoupon.code != it.code &&
                            (selectedPromoCodes.contains(it.code) || selectedSecondaryPromoCodes.contains(it.code))
                    }
                }
                ?: emptyList()
        var state: PromoItemState = if (secondaryCoupon != null) {
            if (coupon.isSelected) {
                PromoItemState.Selected(useSecondaryPromo = false)
            } else if (secondaryCoupon.isSelected) {
                PromoItemState.Selected(useSecondaryPromo = true)
            } else {
                if (primaryClashingInfos.isEmpty() && secondaryClashingInfos.isEmpty()) {
                    PromoItemState.Normal(useSecondaryPromo = false)
                } else if (primaryClashingInfos.isNotEmpty() && secondaryClashingInfos.isEmpty()) {
                    PromoItemState.Normal(useSecondaryPromo = true)
                } else if (primaryClashingInfos.isEmpty()) {
                    PromoItemState.Normal(useSecondaryPromo = false)
                } else {
                    PromoItemState.Disabled(
                        useSecondaryPromo = true,
                        message = secondaryClashingInfos.first().message
                    )
                }
            }
        } else {
            if (coupon.isSelected) {
                if (primaryClashingInfos.isNotEmpty()) {
                    PromoItemState.Disabled(
                        useSecondaryPromo = false,
                        message = primaryClashingInfos.first().message
                    )
                } else {
                    PromoItemState.Selected(useSecondaryPromo = false)
                }
            } else {
                if (primaryClashingInfos.isNotEmpty()) {
                    PromoItemState.Disabled(
                        useSecondaryPromo = false,
                        message = primaryClashingInfos.first().message
                    )
                } else {
                    PromoItemState.Normal(useSecondaryPromo = false)
                }
            }
        }
        if (coupon.radioCheckState == "disabled") {
            state = PromoItemState.Ineligible(message = coupon.message)
        }

        val benefitDetail = coupon.benefitDetails.firstOrNull() ?: BenefitDetail()
        val selectedPromoInSection = couponSection
            .coupons.filter { c -> c.isSelected || c.secondaryCoupon.any { it.isSelected } }
        val isExpanded = when (couponSection.id) {
            PromoPageSection.SECTION_RECOMMENDATION -> {
                true
            }

            else -> {
                if (selectedPromoInSection.isNotEmpty()) {
                    true
                } else {
                    !couponSection.isCollapsed && index.isZero()
                }
            }
        }
        val isVisible = when (couponSection.id) {
            PromoPageSection.SECTION_RECOMMENDATION -> {
                true
            }

            else -> {
                if (selectedPromoInSection.isNotEmpty()) {
                    isSelected
                } else {
                    isExpanded && index.isZero()
                }
            }
        }
        val secondaryPromo = if (secondaryCoupon != null) {
            mapSecondaryCouponToSecondaryPromo(couponSection, secondaryCoupon)
        } else {
            SecondaryPromoItem()
        }
        return PromoItem(
            id = coupon.code,
            headerId = couponSection.id,
            promoId = coupon.id,
            index = coupon.index,
            title = coupon.title,
            uniqueId = coupon.uniqueId,
            shopId = coupon.shopId,
            code = coupon.code,
            message = coupon.message,
            benefitAmount = coupon.benefitAmount,
            benefitAmountStr = coupon.benefitAmountStr,
            benefitDetail = PromoItemBenefitDetail(
                amountIdr = benefitDetail.amountIdr,
                benefitType = benefitDetail.benefitType,
                dataType = benefitDetail.dataType
            ),
            benefitTypeStr = coupon.benefitTypeStr,
            remainingPromoCount = remainingPromoCount,
            cardDetails = mapCardDetails(coupon.couponCardDetails),
            clashingInfos = coupon.clashingInfos.map {
                PromoItemClashingInfo(
                    code = it.code,
                    message = it.message
                )
            },
            promoItemInfos = coupon.promoInfos.map {
                PromoItemInfo(
                    type = it.type,
                    title = it.title,
                    icon = it.icon
                )
            },
            boAdditionalData = coupon.boAdditionalData.map {
                BoAdditionalData(
                    code = it.code,
                    uniqueId = it.uniqueId,
                    cartStringGroup = it.cartStringGroup,
                    shippingId = it.shippingId,
                    spId = it.spId
                )
            },
            expiryInfo = coupon.expiryInfo,
            expiryTimestamp = coupon.expiryCountdown,
            cta = PromoItemCta(
                type = coupon.cta.type,
                text = coupon.cta.text,
                appLink = coupon.cta.applink
            ),
            couponType = coupon.couponType,
            couponUrl = coupon.couponUrl,
            secondaryPromo = secondaryPromo,

            state = state,
            currentClashingPromoCodes = primaryClashingInfos.map { it.code },
            currentClashingSecondaryPromoCodes = secondaryClashingInfos.map { it.code },
            isRecommended = isRecommended,
            isPreSelected = isSelected,
            isAttempted = coupon.isAttempted || secondaryCoupon?.isAttempted ?: false,
            isBebasOngkir = coupon.isBebasOngkir || secondaryCoupon?.isBebasOngkir ?: false,
            isHighlighted = coupon.isHighlighted || secondaryCoupon?.isHighlighted ?: false,
            isLastRecommended = isRecommended && index == couponSection.coupons.size - 1,
            isExpanded = isExpanded,
            isVisible = isVisible
        )
    }

    private fun mapSecondaryCouponToSecondaryPromo(
        couponSection: CouponSection,
        secondaryCoupon: SecondaryCoupon
    ): SecondaryPromoItem {
        val remainingPromoCount = couponSection.couponGroups
            .firstOrNull { it.id == secondaryCoupon.groupId }?.count ?: 1
        val benefitDetail = secondaryCoupon.benefitDetails.firstOrNull() ?: BenefitDetail()
        return SecondaryPromoItem(
            id = secondaryCoupon.code,
            headerId = couponSection.id,
            index = secondaryCoupon.index,
            title = secondaryCoupon.title,
            uniqueId = secondaryCoupon.uniqueId,
            shopId = secondaryCoupon.shopId,
            code = secondaryCoupon.code,
            message = secondaryCoupon.message,
            benefitAmount = secondaryCoupon.benefitAmount,
            benefitAmountStr = secondaryCoupon.benefitAmountStr,
            benefitDetail = PromoItemBenefitDetail(
                amountIdr = benefitDetail.amountIdr,
                benefitType = benefitDetail.benefitType,
                dataType = benefitDetail.dataType
            ),
            benefitTypeStr = secondaryCoupon.benefitTypeStr,
            remainingPromoCount = remainingPromoCount,
            clashingInfos = secondaryCoupon.clashingInfos.map {
                PromoItemClashingInfo(
                    code = it.code,
                    message = it.message
                )
            },
            promoItemInfos = secondaryCoupon.promoInfos.map {
                PromoItemInfo(
                    type = it.type,
                    title = it.title,
                    icon = it.icon
                )
            },
            boAdditionalData = secondaryCoupon.boAdditionalData.map {
                BoAdditionalData(
                    code = it.code,
                    uniqueId = it.uniqueId,
                    cartStringGroup = it.cartStringGroup,
                    shippingId = it.shippingId,
                    spId = it.spId
                )
            },
            expiryInfo = secondaryCoupon.expiryInfo,
            expiryTimestamp = secondaryCoupon.expiryCountdown,
            cta = PromoItemCta(
                type = secondaryCoupon.cta.type,
                text = secondaryCoupon.cta.text,
                appLink = secondaryCoupon.cta.applink
            ),
            couponUrl = secondaryCoupon.couponUrl,
            couponType = secondaryCoupon.couponType
        )
    }

    private fun mapCardDetails(couponCardDetails: List<CouponCardDetail>): List<PromoItemCardDetail> {
        return couponCardDetails.map {
            PromoItemCardDetail(
                state = it.state,
                color = it.color,
                iconUrl = it.iconUrl,
                backgroundUrl = it.backgroundUrl
            )
        }
    }

    fun mapPromoListRecommendationResponseToAttemptedPromoCodeError(
        response: GetPromoListRecommendationResponse
    ): PromoAttemptedError {
        return PromoAttemptedError(
            code = response.promoListRecommendation.data.attemptedPromoCodeError.code,
            message = response.promoListRecommendation.data.attemptedPromoCodeError.message
        )
    }
}
