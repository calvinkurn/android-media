package com.tokopedia.promousage.domain.entity

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class Promo(
    override val id: String = "",
    val index: Int = 0,
    val code: String = "",
    val benefitAmount: Double = 0.0,
    val benefitAmountStr: String = "",
    val benefitDetail: PromoBenefitDetail = PromoBenefitDetail(),
    val benefitTypeStr: String = "",
    val remainingPromoCount: Int = 0,
    val cardDetails: List<PromoCardDetail> = emptyList(),
    val clashingInfos: List<PromoClashingInfo> = emptyList(),
    val boClashingInfos: List<PromoClashingInfo> = emptyList(),
    val promoInfos: List<PromoInfo> = emptyList(),
    val boAdditionalData: List<BoAdditionalData> = emptyList(),
    val expiryInfo: String = "",
    val expiryTimestamp: Long = 0,
    val cta: PromoCta = PromoCta(),
    val couponType: List<String> = emptyList(),

    val state: PromoState = PromoState.Normal,
    val currentClashingPromoCode: List<String> = emptyList(),
    val isRecommended: Boolean = false,
    val isSelected: Boolean = false,
    val isAttempted: Boolean = false,
    val isBebasOngkir: Boolean = false,
    val isHighlighted: Boolean = false,
    val isExpanded: Boolean = false
) : DelegateAdapterItem {

    companion object {
        const val COUPON_TYPE_GOPAY_LATER_CICIL = "gpl_cicil"
    }
}
