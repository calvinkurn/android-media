package com.tokopedia.promousage.view.mapper

import com.tokopedia.promousage.data.DummyData
import com.tokopedia.promousage.data.response.BenefitDetail
import com.tokopedia.promousage.data.response.Coupon
import com.tokopedia.promousage.data.response.CouponSection
import com.tokopedia.promousage.data.response.GetCouponListRecommendationResponse
import com.tokopedia.promousage.data.response.PromoRecommendation
import com.tokopedia.promousage.domain.entity.BoAdditionalData
import com.tokopedia.promousage.domain.entity.PromoItem
import com.tokopedia.promousage.domain.entity.PromoItemBenefitDetail
import com.tokopedia.promousage.domain.entity.PromoItemCardDetail
import com.tokopedia.promousage.domain.entity.PromoItemClashingInfo
import com.tokopedia.promousage.domain.entity.PromoCta
import com.tokopedia.promousage.domain.entity.PromoItemInfo
import com.tokopedia.promousage.domain.entity.PromoPageTickerInfo
import com.tokopedia.promousage.domain.entity.PromoPageSection
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.list.PromoAccordionItem
import com.tokopedia.promousage.domain.entity.list.PromoInputItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import javax.inject.Inject

class PromoUsageMapper @Inject constructor() {

    fun mapCouponListRecommendationResponseToPagetickerInfo(
        response: GetCouponListRecommendationResponse
    ) : PromoPageTickerInfo {
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
        return DummyData.sections

        return response.couponListRecommendation.data.couponSections.map { couponSection ->
            when (couponSection.id) {
                PromoPageSection.SECTION_RECOMMENDATION -> {
                    mapCouponSectionToPromoRecommendation(
                        promoRecommendation = response.couponListRecommendation.data.promoRecommendation,
                        couponSection = couponSection
                    )
                }

                PromoPageSection.SECTION_INPUT_PROMO_CODE -> {
                    PromoInputItem(id = couponSection.id)
                }

                else -> {
                    PromoAccordionItem(
                        id = couponSection.id,
                        title = couponSection.title,
                        sections = couponSection.coupons.map {  coupon ->
                            mapCouponToPromo(couponSection, coupon)
                        }
                    )
                }
            }
        }
    }

    private fun mapCouponSectionToPromoRecommendation(
        promoRecommendation: PromoRecommendation,
        couponSection: CouponSection
    ): PromoRecommendationItem {
        return PromoRecommendationItem(
            id = PromoPageSection.SECTION_RECOMMENDATION,
            title = couponSection.title,
            message = promoRecommendation.message,
            messageSelected = promoRecommendation.messageSelected,
            backgroundUrl = promoRecommendation.backgroundUrl,
            animationUrl = promoRecommendation.animationUrl,
            promos = couponSection.coupons.map { coupon ->
                mapCouponToPromo(couponSection, coupon)
            }
        )
    }

    private fun mapCouponToPromo(
        couponSection: CouponSection,
        coupon: Coupon
    ): PromoItem {
        val firstBenefitDetail = coupon.benefitDetails.firstOrNull() ?: BenefitDetail()
        val remainingPromoCount =
            couponSection.couponGroups.firstOrNull { it.id == coupon.groupId }?.count ?: 1
        return PromoItem(
            id = coupon.id,
            index = coupon.index,
            code = coupon.code,
            benefitAmount = coupon.benefitAmount,
            benefitAmountStr = coupon.benefitAmountStr,
            benefitDetail = PromoItemBenefitDetail(
                amountIdr = firstBenefitDetail.amountIdr,
                benefitType = firstBenefitDetail.benefitType,
                dataType = firstBenefitDetail.dataType
            ),
            benefitTypeStr = coupon.benefitTypeStr,
            remainingPromoCount = remainingPromoCount,
            cardDetails = coupon.couponCardDetails.map {
                PromoItemCardDetail(
                    state = it.state,
                    color = it.color,
                    iconUrl = it.iconUrl,
                    backgroundUrl = it.backgroundUrl
                )
            },
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

            state = PromoItemState.Normal,
            currentClashingPromoCodes = emptyList(),
            isRecommended = coupon.isRecommended,
            isSelected = coupon.isSelected,
            isAttempted = coupon.isAttempted,
            isBebasOngkir = coupon.isBebasOngkir,
            isHighlighted = coupon.isHighlighted,
            isExpanded = coupon.isExpanded
        )
    }
}
