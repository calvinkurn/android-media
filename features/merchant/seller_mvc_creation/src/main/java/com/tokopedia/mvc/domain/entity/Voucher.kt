package com.tokopedia.mvc.domain.entity

import android.os.Parcelable
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import kotlinx.parcelize.Parcelize

@Parcelize
data class Voucher(
    val id: Long = 0,
    val parentId: Long = 0,
    val name: String = "",
    val type: Int = 0,
    val typeFormatted: String = "",
    val image: String = "",
    val imageSquare: String = "",
    val imagePortrait: String = "",
    val status: VoucherStatus = VoucherStatus.PROCESSING,
    val discountUsingPercent: Boolean = false,
    val discountAmt: Int = 0,
    val discountAmtFormatted: String = "",
    val discountAmtMax: Int = 0,
    val minimumAmt: Int = 0,
    val quota: Int = 0,
    val confirmedQuota: Int = 0,
    val bookedQuota: Int = 0,
    val startTime: String = "",
    val finishTime: String = "",
    val code: String = "",
    val createdTime: String = "",
    val updatedTime: String = "",
    val isPublic: Boolean = false,
    val isLockToProduct: Boolean = false,
    val showNewBc: Boolean = false,
    val isFreeIconVisible: Boolean = false,
    val isVps: Boolean = false,
    val totalChild: Int = 0,
    val packageName: String = "",
    val isSubsidy: Boolean = false,
    val tnc: String = "",
    val targetBuyer: VoucherTargetBuyer = VoucherTargetBuyer.ALL_BUYER,
    val discountTypeFormatted: String = "",
    val productIds: List<ProductId> = listOf(),
    val isParent: Boolean = false
) : Parcelable {
    fun isOngoingPromo() = status == VoucherStatus.ONGOING
    fun isUpComingPromo() = status == VoucherStatus.NOT_STARTED

    @Parcelize
    data class ProductId(
        val parentProductId: Long = 0,
        val childProductId: List<Long>? = listOf()
    ) : Parcelable

    fun toVoucherConfiguration(): VoucherConfiguration {
        val selectedParentProductIds = productIds.map { parentProduct -> parentProduct.parentProductId }

        return VoucherConfiguration(
            voucherId = id,
            benefitIdr = discountAmt.toLong(),
            benefitMax = discountAmtMax.toLong(),
            benefitPercent = discountAmt,
            benefitType = if (discountUsingPercent) BenefitType.PERCENTAGE else BenefitType.NOMINAL,
            promoType = PromoType.values().firstOrNull { value -> value.text == typeFormatted }
                ?: PromoType.FREE_SHIPPING,
            isVoucherProduct = isLockToProduct,
            minPurchase = minimumAmt.toLong(),
            productIds = selectedParentProductIds,
            targetBuyer = targetBuyer,
            quota = quota.toLong(),
            isVoucherPublic = isPublic,
            voucherName = name,
            voucherCode = code,
            startPeriod = startTime.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z),
            endPeriod = finishTime.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)
        )
    }
}
