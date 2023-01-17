package com.tokopedia.mvc.domain.entity

import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.utils.date.DateUtil.YYYY_MM_DD_T_HH_MM_SS
import com.tokopedia.utils.date.toDate

data class VoucherDetailData(
    val voucherId: Long = 0,
    val shopId: Long = 0,
    val voucherName: String = "",
    val voucherType: PromoType = PromoType.FREE_SHIPPING,
    val voucherImage: String = "",
    val voucherImageSquare: String = "",
    val voucherImagePortrait: String = "",
    val voucherStatus: VoucherStatus = VoucherStatus.NOT_STARTED,
    val voucherDiscountType: BenefitType = BenefitType.NOMINAL,
    val voucherDiscountAmount: Long = 0,
    val voucherDiscountAmountMax: Long = 0,
    val voucherDiscountAmountMin: Long = 0,
    val voucherQuota: Long = 0,
    val voucherStartTime: String = "",
    val voucherFinishTime: String = "",
    val voucherCode: String = "",
    val galadrielVoucherId: Long = 0,
    val galadrielCatalogId: Long = 0,
    val createTime: String = "",
    val createBy: Long = 0,
    val updateTime: String = "",
    val updateBy: Int = 0,
    val isPublic: Int = 0,
    val isQuotaAvailable: Int = 0,
    val voucherTypeFormatted: String = "",
    val voucherStatusFormatted: String = "",
    val voucherDiscountTypeFormatted: String = "",
    val voucherDiscountAmountFormatted: String = "",
    val voucherDiscountAmountMaxFormatted: String = "",
    val remainingQuota: Long = 0,
    val tnc: String = "",
    val bookedGlobalQuota: Long = 0,
    val confirmedGlobalQuota: Long = 0,
    val targetBuyer: VoucherTargetBuyer = VoucherTargetBuyer.ALL_BUYER,
    val minimumTierLevel: Int = 0,
    val isVoucherProduct: Boolean = false,
    val isVps: Int = 0,
    val packageName: String = "",
    val vpsUniqueId: Long = 0,
    val voucherPackageId: Long = 0,
    val vpsBundlingId: Long = 0,
    val isSubsidy: Int = 0,
    val appLink: String = "",
    val webLink: String = "",
    val warehouseId: Long = 0,
    val voucherMinimumAmountType: Int = 0,
    val voucherMinimumAmountTypeFormatted: String = "",
    val isPeriod: Boolean = false,
    val totalPeriod: Int = 0,
    val voucherLockType: String = "",
    val voucherLockId: Long = 0,
    val productIds: List<ProductId> = listOf()
) {
    data class ProductId(
        val parentProductId: Long = 0,
        val chilProductId: List<Long>? = listOf()
    )

    fun toVoucherConfiguration(): VoucherConfiguration {
        val selectedParentProductIds = productIds.map { parentProduct -> parentProduct.parentProductId }

        return VoucherConfiguration(
            voucherId = voucherId,
            benefitIdr = voucherDiscountAmount,
            benefitMax = voucherDiscountAmountMax,
            benefitPercent = voucherDiscountAmount.toInt(),
            benefitType = voucherDiscountType,
            promoType = voucherType,
            isVoucherProduct = isVoucherProduct,
            minPurchase = voucherDiscountAmountMin,
            productIds = selectedParentProductIds,
            targetBuyer = targetBuyer,
            quota = voucherQuota,
            isVoucherPublic = isPublic.isMoreThanZero(),
            voucherName = voucherName,
            voucherCode = voucherCode,
            startPeriod = voucherStartTime.toDate(YYYY_MM_DD_T_HH_MM_SS),
            endPeriod = voucherFinishTime.toDate(YYYY_MM_DD_T_HH_MM_SS),
            totalPeriod = totalPeriod,
            warehouseId = warehouseId
        )
    }

    fun toSelectedProducts(): List<SelectedProduct> {
        return productIds.map { parentProduct ->
            SelectedProduct(
                parentProduct.parentProductId,
                parentProduct.chilProductId.orEmpty()
            )
        }
    }
}
