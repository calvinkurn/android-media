package com.tokopedia.promousage.view.mapper

import com.tokopedia.promousage.data.DummyData
import com.tokopedia.promousage.data.response.BenefitDetail
import com.tokopedia.promousage.data.response.Coupon
import com.tokopedia.promousage.data.response.CouponCardDetail
import com.tokopedia.promousage.data.response.CouponSection
import com.tokopedia.promousage.data.response.GetCouponListRecommendationResponse
import com.tokopedia.promousage.data.response.PromoRecommendation
import com.tokopedia.promousage.data.response.SecondaryCoupon
import com.tokopedia.promousage.domain.entity.BoAdditionalData
import com.tokopedia.promousage.domain.entity.PromoCta
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.PromoItemBenefitDetail
import com.tokopedia.promousage.domain.entity.PromoItemCardDetail
import com.tokopedia.promousage.domain.entity.PromoItemClashingInfo
import com.tokopedia.promousage.domain.entity.PromoItemInfo
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.PromoPageSection
import com.tokopedia.promousage.domain.entity.PromoPageTickerInfo
import com.tokopedia.promousage.domain.entity.SecondaryPromoItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoInputItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import javax.inject.Inject

class PromoUsageMapper @Inject constructor() {

    fun mapCouponListRecommendationResponseToPageTickerInfo(
        response: GetCouponListRecommendationResponse
    ): PromoPageTickerInfo {
        // TODO: Remove dummy data
        return DummyData.promoPageTickerInfo
        val tickerInfo = response.couponListRecommendation.data.tickerInfo
        return PromoPageTickerInfo(
            message = tickerInfo.message,
            iconUrl = tickerInfo.iconUrl,
            backgroundUrl = tickerInfo.backgroundUrl,
        )
    }

    fun mapCouponListRecommendationResponseToPromoSections(
        response: GetCouponListRecommendationResponse
    ): List<DelegateAdapterItem> {
        // TODO: Remove dummy data
        return DummyData.dummySections()
        val recommendedPromoCodes = response.couponListRecommendation.data.promoRecommendation.codes
        val selectedPromoCodes = response.couponListRecommendation.data.couponSections
            .flatMap { it.coupons }.filter { it.isSelected }.map { it.code }

        val items = mutableListOf<DelegateAdapterItem>()
        response.couponListRecommendation.data.couponSections.map { couponSection ->
            when (couponSection.id) {
                PromoPageSection.SECTION_RECOMMENDATION -> {
                    items.add(
                        mapCouponSectionToPromoRecommendation(
                            promoRecommendation = response.couponListRecommendation.data.promoRecommendation,
                            couponSection = couponSection,
                            recommendedPromoCodes = recommendedPromoCodes,
                            selectedPromoCodes = selectedPromoCodes
                        )
                    )
                }

                PromoPageSection.SECTION_INPUT_PROMO_CODE -> {
                    items.add(PromoInputItem(id = couponSection.id))
                }

                else -> {
                    items.add(
                        PromoAccordionHeaderItem(
                            id = couponSection.id,
                            title = couponSection.title,
                            isExpanded = false
                        )
                    )
                    couponSection.coupons.forEachIndexed { index, coupon ->
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
                    val totalPromoInSectionCount = couponSection.coupons.size
                    if (totalPromoInSectionCount > 1) {
                        val hiddenPromoCount = totalPromoInSectionCount - 1
                        items.add(
                            PromoAccordionViewAllItem(
                                headerId = couponSection.id,
                                visiblePromoCount = hiddenPromoCount,
                                isExpanded = !couponSection.isCollapse,
                                isVisible = !couponSection.isCollapse
                            )
                        )
                    }
                }
            }
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
            backgroundUrl = promoRecommendation.backgroundUrl,
            animationUrl = promoRecommendation.animationUrl,
            promos = couponSection.coupons.mapIndexed { index, coupon ->
                mapCouponToPromo(
                    index = index,
                    couponSection = couponSection,
                    coupon = coupon,
                    recommendedPromoCodes = recommendedPromoCodes,
                    selectedPromoCodes = selectedPromoCodes
                )
            }
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
        val remainingPromoCount =
            couponSection.couponGroups.firstOrNull { it.id == coupon.groupId }?.count ?: 1

        val isRecommended = recommendedPromoCodes.isNotEmpty() &&
            (recommendedPromoCodes.contains(coupon.code)
                || recommendedPromoCodes.contains(secondaryCoupon.code))
        val isSelected = coupon.isSelected || secondaryCoupon.isSelected

        val primaryClashingInfos =
            coupon.clashingInfos.filter { selectedPromoCodes.contains(it.code) }
        val secondaryClashingInfos =
            secondaryCoupon.clashingInfos.filter { selectedPromoCodes.contains(it.code) }
        val hasSecondaryPromo =
            secondaryCoupon.id.isNotBlankOrZero() && secondaryCoupon.code.isNotBlank()
        val useSecondaryPromo =
            primaryClashingInfos.isNotEmpty() && hasSecondaryPromo && secondaryClashingInfos.isEmpty()

        val cardDetails = if (useSecondaryPromo) {
            mapCardDetails(secondaryCoupon.couponCardDetails)
        } else {
            mapCardDetails(coupon.couponCardDetails)
        }
        var state: PromoItemState = if (isSelected) {
            val selectedCardDetail =
                cardDetails.firstOrNull { it.state == PromoItemCardDetail.TYPE_INITIAL }
                    ?: PromoItemCardDetail()
            PromoItemState.Selected(selectedCardDetail)
        } else {
            val normalCardDetail =
                cardDetails.firstOrNull { it.state == PromoItemCardDetail.TYPE_SELECTED }
                    ?: PromoItemCardDetail()
            PromoItemState.Normal(normalCardDetail)
        }
        if (primaryClashingInfos.isNotEmpty()) {
            state = PromoItemState.Disabled(primaryClashingInfos.first().message)
        } else if (secondaryClashingInfos.isNotEmpty()) {
            state = PromoItemState.Disabled(secondaryClashingInfos.first().message)
        }
        if (coupon.radioCheckState == "disabled" && coupon.message.isNotBlank()) {
            state = PromoItemState.Ineligible(coupon.message)
        }

        val benefitDetail = coupon.benefitDetails.firstOrNull() ?: BenefitDetail()

        return PromoItem(
            id = coupon.id,
            headerId = couponSection.id,
            index = coupon.index,
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
                    title = it.title
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
            isSelected = isSelected,
            isAttempted = coupon.isAttempted || secondaryCoupon.isAttempted,
            isBebasOngkir = coupon.isBebasOngkir || secondaryCoupon.isBebasOngkir,
            isHighlighted = coupon.isHighlighted || secondaryCoupon.isHighlighted,
            isExpanded = !couponSection.isCollapse && index == 0,
            isVisible = !couponSection.isCollapse && index == 0,
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
                    title = it.title
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
}
