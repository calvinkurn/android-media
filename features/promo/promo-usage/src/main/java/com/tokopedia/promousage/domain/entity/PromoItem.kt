package com.tokopedia.promousage.domain.entity

import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero

data class PromoItem(
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
    val secondaryPromo: SecondaryPromoItem = SecondaryPromoItem(),

    val state: PromoItemState = PromoItemState.Normal,
    val currentClashingPromoCodes: List<String> = emptyList(),
    val currentClashingSecondaryPromoCodes: List<String> = emptyList(),
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

    val hasClashingPromo: Boolean
        get() = if (secondaryPromo.code.isNotBlank()) {
            currentClashingSecondaryPromoCodes.isNotEmpty()
        } else {
            currentClashingPromoCodes.isNotEmpty()
        }

    val hasSecondaryPromo: Boolean
        get() = secondaryPromo.code.isNotBlank() && secondaryPromo.id.isNotBlankOrZero()

    val useSecondaryPromo: Boolean
        get() = currentClashingPromoCodes.isNotEmpty() && hasSecondaryPromo
            && currentClashingSecondaryPromoCodes.isEmpty()
}
