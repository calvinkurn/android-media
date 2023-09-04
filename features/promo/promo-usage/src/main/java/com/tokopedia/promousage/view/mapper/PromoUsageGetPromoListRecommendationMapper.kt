package com.tokopedia.promousage.view.mapper

import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.network.constant.TkpdBaseURL.Promo
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
import com.tokopedia.promousage.domain.entity.PromoCta
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.promousage.domain.entity.PromoItemBenefitDetail
import com.tokopedia.promousage.domain.entity.PromoItemCardDetail
import com.tokopedia.promousage.domain.entity.PromoItemClashingInfo
import com.tokopedia.promousage.domain.entity.PromoItemInfo
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.PromoPageSection
import com.tokopedia.promousage.domain.entity.PromoPageTickerInfo
import com.tokopedia.promousage.domain.entity.PromoSavingInfo
import com.tokopedia.promousage.domain.entity.SecondaryPromoItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoAttemptItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
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
            backgroundUrl = response.promoListRecommendation.data.tickerInfo.backgroundUrl,
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
                                selectedPromoCodes = selectedPromoCodes
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
                                        selectedPromoCodes = selectedPromoCodes
                                    )
                                )
                            }
                    }
                }

                else -> {
                    if (couponSection.coupons.isNotEmpty()) {
                        hasRecommendedOrOtherSection = true
                        items.add(
                            PromoAccordionHeaderItem(
                                id = couponSection.id,
                                title = couponSection.title,
                                isExpanded = !couponSection.isCollapsed
                            )
                        )
                        val coupons = couponSection.coupons.filter { it.isGroupHeader }
                        coupons.forEachIndexed { index, coupon ->
                                items.add(
                                    mapCouponToPromo(
                                        index = index,
                                        couponSection = couponSection,
                                        coupon = coupon,
                                        recommendedPromoCodes = recommendedPromoCodes,
                                        selectedPromoCodes = selectedPromoCodes
                                    )
                                )
                            }
                        val totalPromoInSectionCount = coupons.size
                        if (totalPromoInSectionCount > 1) {
                            val isExpanded = !couponSection.isCollapsed
                            if (isExpanded) {
                                val hiddenPromoCount = totalPromoInSectionCount - 1
                                items.add(
                                    PromoAccordionViewAllItem(
                                        headerId = couponSection.id,
                                        hiddenPromoCount = hiddenPromoCount,
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
                errorMessage = attemptedPromoError.message,
                hasOtherSection = hasRecommendedOrOtherSection
            )
        )
        attemptedPromoSection?.coupons?.forEachIndexed { index, coupon ->
            items.add(
                mapCouponToPromo(
                    index = index,
                    couponSection = attemptedPromoSection,
                    coupon = coupon,
                    recommendedPromoCodes = recommendedPromoCodes,
                    selectedPromoCodes = selectedPromoCodes
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
            animationUrl = promoRecommendation.animationUrl
        )
    }

    private fun mapCouponToPromo(
        index: Int,
        couponSection: CouponSection,
        coupon: Coupon,
        recommendedPromoCodes: List<String>,
        selectedPromoCodes: List<String>,
    ): PromoItem {
        val secondaryCoupon: SecondaryCoupon = if (coupon.secondaryCoupon.isNotEmpty()) {
            coupon.secondaryCoupon.first()
        } else {
            SecondaryCoupon()
        }
        val remainingPromoCount = couponSection.couponGroups
            .firstOrNull { it.id == coupon.groupId }?.count ?: 1

        val isRecommended = recommendedPromoCodes.isNotEmpty() &&
            (recommendedPromoCodes.contains(coupon.code)
                || recommendedPromoCodes.contains(secondaryCoupon.code))
        val isSelected = coupon.isSelected || secondaryCoupon.isSelected

        val primaryClashingInfos =
            coupon.clashingInfos.filter { selectedPromoCodes.contains(it.code) }
        val secondaryClashingInfos =
            secondaryCoupon.clashingInfos.filter { selectedPromoCodes.contains(it.code) }

        var state: PromoItemState = if (isSelected) {
            PromoItemState.Selected
        } else {
            PromoItemState.Normal
        }
        if (primaryClashingInfos.isNotEmpty()) {
            state = PromoItemState.Disabled(primaryClashingInfos.first().message)
        } else if (secondaryClashingInfos.isNotEmpty()) {
            state = PromoItemState.Disabled(secondaryClashingInfos.first().message)
        }
        if (coupon.radioCheckState == "disabled") {
            state = PromoItemState.Ineligible(coupon.message)
        }

        val benefitDetail = coupon.benefitDetails.firstOrNull() ?: BenefitDetail()
        val isExpanded = when(couponSection.id) {
            PromoPageSection.SECTION_RECOMMENDATION -> {
                true
            }

            else -> {
                !couponSection.isCollapsed && index.isZero()
            }
        }
        val isVisible = when (couponSection.id) {
            PromoPageSection.SECTION_RECOMMENDATION -> {
                true
            }

            else -> {
                isExpanded && index.isZero()
            }
        }
        return PromoItem(
            id = coupon.id,
            headerId = couponSection.id,
            index = coupon.index,
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
                    spId = it.spId,

                    )
            },
            expiryInfo = coupon.expiryInfo,
            expiryTimestamp = coupon.expiryCountdown,
            cta = PromoCta(
                type = coupon.cta.type,
                text = coupon.cta.text,
                appLink = coupon.cta.applink
            ),
            couponType = coupon.couponType,
            secondaryPromo = mapSecondaryCouponToSecondaryPromo(couponSection, secondaryCoupon),

            state = state,
            currentClashingPromoCodes = primaryClashingInfos.map { it.code },
            currentClashingSecondaryPromoCodes = secondaryClashingInfos.map { it.code },
            isRecommended = isRecommended,
            isPreSelected = isSelected,
            isAttempted = coupon.isAttempted || secondaryCoupon.isAttempted,
            isBebasOngkir = coupon.isBebasOngkir || secondaryCoupon.isBebasOngkir,
            isHighlighted = coupon.isHighlighted || secondaryCoupon.isHighlighted,
            isLastRecommended = isRecommended && index == couponSection.coupons.size - 1,
            isExpanded = isExpanded,
            isVisible = isVisible,
        )
    }

    private fun mapSecondaryCouponToSecondaryPromo(
        couponSection: CouponSection,
        secondaryCoupon: SecondaryCoupon
    ): SecondaryPromoItem {
        val benefitDetail = secondaryCoupon.benefitDetails.firstOrNull() ?: BenefitDetail()
        return SecondaryPromoItem(
            id = secondaryCoupon.id,
            headerId = couponSection.id,
            index = secondaryCoupon.index,
            uniqueId = secondaryCoupon.uniqueId,
            shopId = secondaryCoupon.shopId,
            code = secondaryCoupon.code,
            benefitAmount = secondaryCoupon.benefitAmount,
            benefitAmountStr = secondaryCoupon.benefitAmountStr,
            benefitDetail = PromoItemBenefitDetail(
                amountIdr = benefitDetail.amountIdr,
                benefitType = benefitDetail.benefitType,
                dataType = benefitDetail.dataType
            ),
            benefitTypeStr = secondaryCoupon.benefitTypeStr,
            cardDetails = mapCardDetails(secondaryCoupon.couponCardDetails),
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
                    spId = it.spId,

                    )
            },
            expiryInfo = secondaryCoupon.expiryInfo,
            expiryTimestamp = secondaryCoupon.expiryCountdown,
            cta = PromoCta(
                type = secondaryCoupon.cta.type,
                text = secondaryCoupon.cta.text,
                appLink = secondaryCoupon.cta.applink
            ),
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
