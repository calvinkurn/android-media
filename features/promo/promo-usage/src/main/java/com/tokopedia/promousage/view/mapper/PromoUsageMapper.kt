package com.tokopedia.promousage.view.mapper

import com.tokopedia.promousage.data.response.BenefitDetail
import com.tokopedia.promousage.data.response.Coupon
import com.tokopedia.promousage.data.response.CouponSection
import com.tokopedia.promousage.data.response.GetCouponListRecommendationResponse
import com.tokopedia.promousage.data.response.PromoRecommendation
import com.tokopedia.promousage.domain.entity.BoAdditionalData
import com.tokopedia.promousage.domain.entity.Promo
import com.tokopedia.promousage.domain.entity.PromoBenefitDetail
import com.tokopedia.promousage.domain.entity.PromoCardDetail
import com.tokopedia.promousage.domain.entity.PromoClashingInfo
import com.tokopedia.promousage.domain.entity.PromoCta
import com.tokopedia.promousage.domain.entity.PromoInfo
import com.tokopedia.promousage.domain.entity.PromoSection
import com.tokopedia.promousage.domain.entity.PromoState
import com.tokopedia.promousage.domain.entity.list.PromoAccordionItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoInputItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import javax.inject.Inject

class PromoUsageMapper @Inject constructor() {

    fun mapCouponListRecommendationResponseToPromoSections(
        response: GetCouponListRecommendationResponse
    ): List<DelegateAdapterItem> {
        return response.couponListRecommendation.data.couponSections.map { couponSection ->
            when (couponSection.id) {
                PromoSection.SECTION_RECOMMENDATION -> {
                    mapCouponSectionToPromoRecommendation(
                        promoRecommendation = response.couponListRecommendation.data.promoRecommendation,
                        couponSection = couponSection
                    )
                }

                PromoSection.SECTION_INPUT_PROMO_CODE -> {
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
            id = PromoSection.SECTION_RECOMMENDATION,
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
    ): Promo {
        val firstBenefitDetail = coupon.benefitDetails.firstOrNull() ?: BenefitDetail()
        val remainingPromoCount =
            couponSection.couponGroups.firstOrNull { it.id == coupon.groupId }?.count ?: 1
        return Promo(
            id = coupon.id,
            index = coupon.index,
            code = coupon.code,
            benefitAmount = coupon.benefitAmount,
            benefitAmountStr = coupon.benefitAmountStr,
            benefitDetail = PromoBenefitDetail(
                amountIdr = firstBenefitDetail.amountIdr,
                benefitType = firstBenefitDetail.benefitType,
                dataType = firstBenefitDetail.dataType
            ),
            benefitTypeStr = coupon.benefitTypeStr,
            remainingPromoCount = remainingPromoCount,
            cardDetails = coupon.couponCardDetails.map {
                PromoCardDetail(
                    state = it.state,
                    color = it.color,
                    iconUrl = it.iconUrl,
                    backgroundUrl = it.backgroundUrl
                )
            },
            clashingInfos = coupon.clashingInfos.map {
                PromoClashingInfo(
                    code = it.code,
                    message = it.message
                )
            },
            boClashingInfos = coupon.boClashingInfo.map {
                PromoClashingInfo(
                    code = it.code,
                    message = it.message
                )
            },
            promoInfos = coupon.promoInfos.map {
                PromoInfo(
                    type = it.type,
                    title = it.title
                )
            },
            boAdditionalData = coupon.boAdditionalData.map {
                BoAdditionalData(
                    promoId = it.promoId,
                    code = it.code,
                    uniqueId = it.uniqueId,
                    cartStringGroup = it.cartStringGroup,
                    shippingId = it.shippingId,
                    spId = it.shipperProductId,
                    benefitAmount = it.benefitAmount,
                    shippingPrice = it.shippingPrice,
                    shippingSubsidy = it.shippingSubsidy,
                    benefitClass = it.benefitClass,
                    boCampaignId = it.boCampaignId,
                    etaText = it.etaText
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

            state = PromoState.Normal,
            currentClashingPromoCode = emptyList(),
            isRecommended = coupon.isRecommended,
            isSelected = coupon.isSelected,
            isAttempted = coupon.isAttempted,
            isBebasOngkir = coupon.isBebasOngkir,
            isHighlighted = coupon.isHighlighted,
            isExpanded = coupon.isExpanded
        )
    }
}

fun List<Promo>.toCollapsibleList(): List<DelegateAdapterItem> {
    if (isEmpty()) return emptyList()

    val formattedVouchers = mutableListOf<DelegateAdapterItem>()

    val numberOfExpandedVoucher = count { it.isExpanded }

    val lastIndexOfExpandedVoucher = indexOfLast { it.isExpanded }

    val collapsedVoucherCount = size - numberOfExpandedVoucher
    val viewAllWidgetItem = PromoAccordionViewAllItem(promoCount = collapsedVoucherCount)

    formattedVouchers.addAll(this)

    //Modify the list ordering. Placing view all widget item after last index of expanded voucher
    formattedVouchers.add(lastIndexOfExpandedVoucher + 1, viewAllWidgetItem)

    return formattedVouchers
}

fun List<DelegateAdapterItem>.viewAll(): List<DelegateAdapterItem> {
    //Inside VoucherAccordion there are 2 items [Voucher, ViewAllVoucher] model
    val updatedVoucherAccordionItems = this.toMutableList()

    //Remove ViewAllVoucher to make view all CTA gone
    updatedVoucherAccordionItems.removeAll { it is PromoAccordionViewAllItem }

    //Change Voucher visible state to true to make all voucher visible
    val expandedVouchers = updatedVoucherAccordionItems.map { item ->
        val promo = item as Promo
        promo.copy(isExpanded = true)
    }

    return expandedVouchers
}
