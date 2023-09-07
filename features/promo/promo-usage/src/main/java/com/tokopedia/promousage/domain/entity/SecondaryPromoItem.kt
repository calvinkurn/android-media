package com.tokopedia.promousage.domain.entity

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class SecondaryPromoItem(
    override val id: String = "",
    val headerId: String = "",
    val index: Int = 0,
    val code: String = "",
    val uniqueId: String = "",
    val shopId: Long = 0,
    val message: String = "",
    val benefitAmount: Double = 0.0,
    val benefitAmountStr: String = "",
    val benefitDetail: PromoItemBenefitDetail = PromoItemBenefitDetail(),
    val benefitTypeStr: String = "",
    val remainingPromoCount: Int = 0,
    val cardDetails: List<PromoItemCardDetail> = emptyList(),
    val clashingInfos: List<PromoItemClashingInfo> = emptyList(),
    val promoItemInfos: List<PromoItemInfo> = emptyList(),
    val boAdditionalData: List<BoAdditionalData> = emptyList(),
    val expiryInfo: String = "",
    val expiryTimestamp: Long = 0,
    val cta: PromoCta = PromoCta(),
    val couponUrl: String = "",
    val couponType: List<String> = emptyList(),
) : DelegateAdapterItem
