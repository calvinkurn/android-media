package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.domain.entity.BoAdditionalData
import com.tokopedia.promousage.domain.entity.PromoItemBenefitDetail
import com.tokopedia.promousage.domain.entity.PromoItemCardDetail
import com.tokopedia.promousage.domain.entity.PromoItemClashingInfo
import com.tokopedia.promousage.domain.entity.PromoItemCta
import com.tokopedia.promousage.domain.entity.PromoItemInfo
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.util.analytics.model.ImpressHolder
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.promousage.util.composite.DelegatePayload
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromoItem(
    override val id: String = "",
    val headerId: String = "",

    val promoId: String = "",
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
    val cta: PromoItemCta = PromoItemCta(),
    val couponUrl: String = "",
    val couponType: List<String> = emptyList(),
    val secondaryPromo: SecondaryPromoItem = SecondaryPromoItem(),

    val errorMessage: String = "",

    val state: PromoItemState = PromoItemState.Loading(false),
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
        get() = secondaryPromo.code.isNotBlank() && secondaryPromo.id.isNotBlank()

    private val isUseSecondaryPromoNormalState
        get() = hasSecondaryPromo && state is PromoItemState.Normal && state.useSecondaryPromo

    private val isUseSecondaryPromoSelectedState
        get() = hasSecondaryPromo && state is PromoItemState.Selected && state.useSecondaryPromo

    private val isUseSecondaryPromoDisabledState
        get() = hasSecondaryPromo && state is PromoItemState.Disabled && state.useSecondaryPromo

    val useSecondaryPromo: Boolean
        get() = isUseSecondaryPromoNormalState ||
            isUseSecondaryPromoSelectedState || isUseSecondaryPromoDisabledState

    val isPromoGopayLater: Boolean
        get() = if (useSecondaryPromo) {
            secondaryPromo.couponType.firstOrNull {
                it == PromoItem.COUPON_TYPE_GOPAY_LATER_CICIL
            } != null
        } else {
            couponType.firstOrNull {
                it == PromoItem.COUPON_TYPE_GOPAY_LATER_CICIL
            } != null
        }

    val isPromoCtaRegisterGopayLater: Boolean
        get() = if (useSecondaryPromo) {
        secondaryPromo.cta.type == PromoItemCta.TYPE_REGISTER_GOPAY_LATER_CICIL
    } else {
        cta.type == PromoItemCta.TYPE_REGISTER_GOPAY_LATER_CICIL
    }

    val isPromoCtaValid: Boolean
        get() = if (useSecondaryPromo) {
        secondaryPromo.cta.text.isNotBlank() &&
            secondaryPromo.cta.appLink.isNotBlank()
    } else {
        cta.text.isNotBlank() && cta.appLink.isNotBlank()
    }

    override fun getChangePayload(other: Any): Any? {
        if (other is PromoItem && id == other.id) {
            val isPromoStateUpdated = state != other.state ||
                useSecondaryPromo != other.useSecondaryPromo ||
                isCalculating != other.isCalculating ||
                isExpanded != other.isExpanded ||
                isVisible != other.isVisible
            return DelegatePayload.UpdatePromo(
                isReload = false,
                isPromoStateUpdated = isPromoStateUpdated
            )
        }
        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PromoItem

        if (id != other.id) return false
        if (headerId != other.headerId) return false
        if (index != other.index) return false
        if (title != other.title) return false
        if (code != other.code) return false
        if (uniqueId != other.uniqueId) return false
        if (shopId != other.shopId) return false
        if (message != other.message) return false
        if (benefitAmount != other.benefitAmount) return false
        if (benefitAmountStr != other.benefitAmountStr) return false
        if (benefitDetail != other.benefitDetail) return false
        if (benefitTypeStr != other.benefitTypeStr) return false
        if (remainingPromoCount != other.remainingPromoCount) return false
        if (cardDetails != other.cardDetails) return false
        if (clashingInfos != other.clashingInfos) return false
        if (promoItemInfos != other.promoItemInfos) return false
        if (boAdditionalData != other.boAdditionalData) return false
        if (expiryInfo != other.expiryInfo) return false
        if (expiryTimestamp != other.expiryTimestamp) return false
        if (cta != other.cta) return false
        if (couponUrl != other.couponUrl) return false
        if (couponType != other.couponType) return false
        if (secondaryPromo != other.secondaryPromo) return false
        if (errorMessage != other.errorMessage) return false
        if (state != other.state) return false
        if (currentClashingPromoCodes != other.currentClashingPromoCodes) return false
        if (currentClashingSecondaryPromoCodes != other.currentClashingSecondaryPromoCodes) return false
        if (isRecommended != other.isRecommended) return false
        if (isLastRecommended != other.isLastRecommended) return false
        if (isPreSelected != other.isPreSelected) return false
        if (isAttempted != other.isAttempted) return false
        if (isBebasOngkir != other.isBebasOngkir) return false
        if (isHighlighted != other.isHighlighted) return false
        if (isExpanded != other.isExpanded) return false
        if (isVisible != other.isVisible) return false
        if (isCausingOtherPromoClash != other.isCausingOtherPromoClash) return false
        if (isCalculating != other.isCalculating) return false
        if (isInvoke != other.isInvoke) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + headerId.hashCode()
        result = 31 * result + index
        result = 31 * result + title.hashCode()
        result = 31 * result + code.hashCode()
        result = 31 * result + uniqueId.hashCode()
        result = 31 * result + shopId.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + benefitAmount.hashCode()
        result = 31 * result + benefitAmountStr.hashCode()
        result = 31 * result + benefitDetail.hashCode()
        result = 31 * result + benefitTypeStr.hashCode()
        result = 31 * result + remainingPromoCount
        result = 31 * result + cardDetails.hashCode()
        result = 31 * result + clashingInfos.hashCode()
        result = 31 * result + promoItemInfos.hashCode()
        result = 31 * result + boAdditionalData.hashCode()
        result = 31 * result + expiryInfo.hashCode()
        result = 31 * result + expiryTimestamp.hashCode()
        result = 31 * result + cta.hashCode()
        result = 31 * result + couponUrl.hashCode()
        result = 31 * result + couponType.hashCode()
        result = 31 * result + secondaryPromo.hashCode()
        result = 31 * result + errorMessage.hashCode()
        result = 31 * result + state.hashCode()
        result = 31 * result + currentClashingPromoCodes.hashCode()
        result = 31 * result + currentClashingSecondaryPromoCodes.hashCode()
        result = 31 * result + isRecommended.hashCode()
        result = 31 * result + isLastRecommended.hashCode()
        result = 31 * result + isPreSelected.hashCode()
        result = 31 * result + isAttempted.hashCode()
        result = 31 * result + isBebasOngkir.hashCode()
        result = 31 * result + isHighlighted.hashCode()
        result = 31 * result + isExpanded.hashCode()
        result = 31 * result + isVisible.hashCode()
        result = 31 * result + isCausingOtherPromoClash.hashCode()
        result = 31 * result + isCalculating.hashCode()
        result = 31 * result + isInvoke.hashCode()
        return result
    }
}
