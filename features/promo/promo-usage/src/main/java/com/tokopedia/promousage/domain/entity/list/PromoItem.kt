package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.domain.entity.BoAdditionalData
import com.tokopedia.promousage.domain.entity.PromoCta
import com.tokopedia.promousage.domain.entity.PromoItemBenefitDetail
import com.tokopedia.promousage.domain.entity.PromoItemCardDetail
import com.tokopedia.promousage.domain.entity.PromoItemClashingInfo
import com.tokopedia.promousage.domain.entity.PromoItemInfo
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.SecondaryPromoItem
import com.tokopedia.promousage.util.analytics.model.ImpressHolder
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero

data class PromoItem(
    override val id: String = "",
    val headerId: String = "",

    val index: Int = 0,
    val title: String = "",
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
    val secondaryPromo: SecondaryPromoItem = SecondaryPromoItem(),

    val errorMessage: String = "",

    val state: PromoItemState = PromoItemState.Loading,
    val currentClashingPromoCodes: List<String> = emptyList(),
    val currentClashingSecondaryPromoCodes: List<String> = emptyList(),
    val isRecommended: Boolean = false,
    val isLastRecommended: Boolean = false,
    val isPreSelected: Boolean = false,
    val isAttempted: Boolean = false,
    val isBebasOngkir: Boolean = false,
    val isHighlighted: Boolean = false,
    val isExpanded: Boolean = false,
    val isVisible: Boolean = false,
    val isCausingOtherPromoClash: Boolean = false,
    val isCalculating: Boolean = false,

    override var isInvoke: Boolean = false
) : DelegateAdapterItem, ImpressHolder() {

    companion object {
        const val COUPON_TYPE_GOPAY_LATER_CICIL = "gopay_later"
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
        get() = currentClashingPromoCodes.isNotEmpty() && hasSecondaryPromo &&
            currentClashingSecondaryPromoCodes.isEmpty()
}
