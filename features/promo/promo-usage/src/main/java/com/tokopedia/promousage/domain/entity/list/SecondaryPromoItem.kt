package com.tokopedia.promousage.domain.entity.list

import android.os.Parcelable
import com.tokopedia.promousage.domain.entity.BoAdditionalData
import com.tokopedia.promousage.domain.entity.PromoItemBenefitDetail
import com.tokopedia.promousage.domain.entity.PromoItemClashingInfo
import com.tokopedia.promousage.domain.entity.PromoItemCta
import com.tokopedia.promousage.domain.entity.PromoItemInfo
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class SecondaryPromoItem(
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
    val clashingInfos: List<PromoItemClashingInfo> = emptyList(),
    val promoItemInfos: List<PromoItemInfo> = emptyList(),
    val boAdditionalData: List<BoAdditionalData> = emptyList(),
    val expiryInfo: String = "",
    val expiryTimestamp: Long = 0,
    val cta: PromoItemCta = PromoItemCta(),
    val couponUrl: String = "",
    val couponType: List<String> = emptyList()
) : Parcelable, DelegateAdapterItem {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SecondaryPromoItem

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
        if (clashingInfos != other.clashingInfos) return false
        if (promoItemInfos != other.promoItemInfos) return false
        if (boAdditionalData != other.boAdditionalData) return false
        if (expiryInfo != other.expiryInfo) return false
        if (expiryTimestamp != other.expiryTimestamp) return false
        if (cta != other.cta) return false
        if (couponUrl != other.couponUrl) return false
        if (couponType != other.couponType) return false

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
        result = 31 * result + clashingInfos.hashCode()
        result = 31 * result + promoItemInfos.hashCode()
        result = 31 * result + boAdditionalData.hashCode()
        result = 31 * result + expiryInfo.hashCode()
        result = 31 * result + expiryTimestamp.hashCode()
        result = 31 * result + cta.hashCode()
        result = 31 * result + couponUrl.hashCode()
        result = 31 * result + couponType.hashCode()
        return result
    }
}
