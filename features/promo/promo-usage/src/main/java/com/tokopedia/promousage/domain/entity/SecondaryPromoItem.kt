package com.tokopedia.promousage.domain.entity

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class SecondaryPromoItem(
    override val id: String = "",
    val index: Int = 0,
    val code: String = "",
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
    val couponType: List<String> = emptyList(),
) : DelegateAdapterItem
