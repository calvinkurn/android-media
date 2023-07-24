package com.tokopedia.promousage.domain.entity

import androidx.annotation.StringDef
import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class Promo(
    val id: String = "",
    val index: Int = 0,
    val code: String = "",
    val benefitAmount: Long = 0,
    val benefitAmountStr: String = "",
    @BenefitType val benefitType: String = BENEFIT_TYPE_UNKNOWN,
    val cardDetails: List<PromoCardDetail> = emptyList(),
    val clashingInfos: List<PromoClashingInfo> = emptyList(),
    val boClashingInfos: List<PromoClashingInfo> = emptyList(),
    val promoInfos: List<PromoInfo> = emptyList(),
    val boAdditionalData: List<BoAdditionalData> = emptyList(),
    val expiryInfo: String = "",
    val expiryTimestamp: Long = 0,
    val cta: PromoCta = PromoCta(),

    val state: PromoState = PromoState.Normal,
    val currentClashingPromoCode: List<String> = emptyList(),
    val isAttempted: Boolean = false,
    val isVisible: Boolean = false
) : DelegateAdapterItem {
    override fun id() = id

    companion object {

        @StringDef(
            BENEFIT_TYPE_CASHBACK,
            BENEFIT_TYPE_DISCOUNT,
            BENEFIT_TYPE_FREE_SHIPPING,
            BENEFIT_TYPE_UNKNOWN
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class BenefitType

        const val BENEFIT_TYPE_CASHBACK = "cashback"
        const val BENEFIT_TYPE_DISCOUNT = "discount"
        const val BENEFIT_TYPE_FREE_SHIPPING = "gratis_ongkir"
        const val BENEFIT_TYPE_UNKNOWN = "unknown"
    }
}
