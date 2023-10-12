package com.tokopedia.mvc.domain.entity

import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.ProgramStatus
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.PromotionStatus
import com.tokopedia.mvc.domain.entity.enums.SubsidyInfo
import com.tokopedia.mvc.domain.entity.enums.VoucherCreator
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
    val isChild: Int = 0,
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
    val productIds: List<ProductId> = listOf(),
    val labelVoucher: LabelVoucher = LabelVoucher(),
    val isEditable: Boolean = false,
    val subsidyDetail: SubsidyDetail = SubsidyDetail()
) {
    data class ProductId(
        val parentProductId: Long = 0,
        val chilProductId: List<Long>? = listOf()
    )

    data class LabelVoucher(
        val labelQuota: Int = 0,
        val labelQuotaFormatted: String = "",
        val labelQuotaColorType: String = "default",
        val labelCreator: VoucherCreator = VoucherCreator.SELLER,
        val labelCreatorFormatted: String = "",
        val labelCreatorColorType: String = "default",
        val labelSubsidyInfo: SubsidyInfo = SubsidyInfo.NOT_SUBSIDIZED,
        val labelSubsidyInfoFormatted: String = "",
        val labelSubsidyInfoColorType: String = "default",
        val labelBudgetsVoucher: List<LabelBudgetVoucher> = listOf()
    ) {
        data class LabelBudgetVoucher(
            val labelBudgetVoucher: Int = 0,
            val labelBudgetVoucherFormatted: String = "",
            val labelBudgetVoucherValue: Int = 0
        )
    }

    data class SubsidyDetail(
        val programDetail: ProgramDetail = ProgramDetail(),
        val quotaSubsidized: QuotaSubsidized = QuotaSubsidized()
    ) {
        data class ProgramDetail(
            val programName: String = "",
            val programStatus: ProgramStatus = ProgramStatus.ONGOING,
            val programLabel: String = "",
            val programLabelDetail: String = "",
            val promotionStatus: PromotionStatus = PromotionStatus.REGISTERED,
            val promotionLabel: String = ""
        )

        data class QuotaSubsidized(
            val voucherQuota: Int = 0,
            val remainingQuota: Int = 0,
            val bookedGlobalQuota: Int = 0,
            val confirmedGlobalQuota: Int = 0
        )
    }

    fun toVoucherConfiguration(): VoucherConfiguration {
        val selectedParentProductIds =
            productIds.map { parentProduct -> parentProduct.parentProductId }

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
            warehouseId = warehouseId,
            totalPeriod = totalPeriod,
            isPeriod = isPeriod
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

    fun isGetSubsidy(): Boolean {
        return when (labelVoucher.labelSubsidyInfo) {
            SubsidyInfo.NOT_SUBSIDIZED -> {
                false
            }

            SubsidyInfo.FULL_SUBSIDIZED, SubsidyInfo.PARTIALLY_SUBSIDIZED -> {
                true
            }
        }
    }

    fun isFromVps(): Boolean {
        return labelVoucher.labelCreator == VoucherCreator.VPS
    }
}
