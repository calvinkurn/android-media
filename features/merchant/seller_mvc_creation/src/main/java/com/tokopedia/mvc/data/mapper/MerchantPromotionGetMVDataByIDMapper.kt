package com.tokopedia.mvc.data.mapper

import com.tokopedia.mvc.data.response.MerchantPromotionGetMVDataByIDResponse
import com.tokopedia.mvc.domain.entity.VoucherDetailData
import com.tokopedia.mvc.domain.entity.VoucherDetailData.SubsidyDetail.ProgramDetail
import com.tokopedia.mvc.domain.entity.VoucherDetailData.SubsidyDetail.QuotaSubsidized
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.ProgramStatus
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.PromotionStatus
import com.tokopedia.mvc.domain.entity.enums.SubsidyInfo
import com.tokopedia.mvc.domain.entity.enums.VoucherCreator
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.util.constant.DiscountTypeConstant
import javax.inject.Inject

class MerchantPromotionGetMVDataByIDMapper @Inject constructor() {

    companion object {
        private const val TRUE = 1
        private const val MIN_VALUE_TOTAL_PERIOD = 1
    }

    fun map(response: MerchantPromotionGetMVDataByIDResponse): VoucherDetailData {
        return with(response.merchantPromotionGetMVDataByID.data) {
            val voucherDetailData = VoucherDetailData(
                voucherId = voucherId,
                shopId = shopId,
                voucherName = voucherName,
                voucherType = PromoType.values().firstOrNull { value -> value.id == voucherType }
                    ?: PromoType.FREE_SHIPPING,
                voucherImage = voucherImage,
                voucherImageSquare = voucherImageSquare,
                voucherImagePortrait = voucherImagePortrait,
                voucherStatus = VoucherStatus.values().firstOrNull { value -> value.id == voucherStatus }
                    ?: VoucherStatus.PROCESSING,
                voucherDiscountType = voucherDiscountType.toBenefitType(),
                voucherDiscountAmount = voucherDiscountAmount,
                voucherDiscountAmountMax = voucherDiscountAmountMax,
                voucherDiscountAmountMin = voucherDiscountAmountMin,
                voucherQuota = voucherQuota,
                voucherStartTime = voucherStartTime,
                voucherFinishTime = voucherFinishTime,
                voucherCode = voucherCode,
                galadrielVoucherId = galadrielVoucherId,
                galadrielCatalogId = galadrielCatalogId,
                createTime = createTime,
                createBy = createBy,
                updateTime = updateTime,
                updateBy = updateBy,
                isPublic = isPublic,
                isChild = isChild,
                isQuotaAvailable = isQuotaAvailable,
                voucherTypeFormatted = voucherTypeFormatted,
                voucherStatusFormatted = voucherStatusFormatted,
                voucherDiscountTypeFormatted = voucherDiscountTypeFormatted,
                voucherDiscountAmountFormatted = voucherDiscountAmountFormatted,
                voucherDiscountAmountMaxFormatted = voucherDiscountAmountMaxFormatted,
                remainingQuota = remainingQuota,
                tnc = tnc,
                bookedGlobalQuota = bookedGlobalQuota,
                confirmedGlobalQuota = confirmedGlobalQuota,
                targetBuyer = VoucherTargetBuyer.values().firstOrNull { value -> value.id == targetBuyer }
                    ?: VoucherTargetBuyer.ALL_BUYER,
                minimumTierLevel = minimumTierLevel,
                isVoucherProduct = isLockToProduct == TRUE,
                isVps = isVps,
                packageName = packageName,
                vpsUniqueId = vpsUniqueId,
                voucherPackageId = voucherPackageId,
                vpsBundlingId = vpsBundlingId,
                isSubsidy = isSubsidy,
                appLink = appLink,
                webLink = webLink,
                warehouseId = warehouseId,
                voucherMinimumAmountType = voucherMinimumAmountType,
                voucherMinimumAmountTypeFormatted = voucherMinimumAmountTypeFormatted,
                isPeriod = isPeriod,
                totalPeriod = totalPeriod.coerceAtLeast(MIN_VALUE_TOTAL_PERIOD),
                voucherLockType = voucherLockType,
                voucherLockId = voucherLockId,
                productIds = toProductIds(),
                labelVoucher = toLabelVoucher(),
                isEditable = isEditable,
                subsidyDetail = toSubsidyDetail()
            )
            voucherDetailData
        }
    }

    private fun MerchantPromotionGetMVDataByIDResponse.MerchantPromotionGetMVDataByID.Data.toProductIds(): List<VoucherDetailData.ProductId> {
        return productIds.map {
            VoucherDetailData.ProductId(
                it.parentProductId,
                it.chilProductId
            )
        }
    }

    private fun MerchantPromotionGetMVDataByIDResponse.MerchantPromotionGetMVDataByID.Data.toLabelVoucher(): VoucherDetailData.LabelVoucher {
        return VoucherDetailData.LabelVoucher(
            labelQuota = labelVoucher.labelQuota,
            labelQuotaFormatted = labelVoucher.labelQuotaFormatted,
            labelQuotaColorType = labelVoucher.labelQuotaColorType,
            labelCreator = VoucherCreator.values().firstOrNull { value ->
                value.id == labelVoucher.labelCreator
            } ?: VoucherCreator.SELLER,
            labelCreatorFormatted = labelVoucher.labelCreatorFormatted,
            labelCreatorColorType = labelVoucher.labelCreatorColorType,
            labelSubsidyInfo = SubsidyInfo.values().firstOrNull { value ->
                value.id == labelVoucher.labelSubsidyInfo
            } ?: SubsidyInfo.NOT_SUBSIDIZED,
            labelSubsidyInfoFormatted = labelVoucher.labelSubsidyInfoFormatted,
            labelSubsidyInfoColorType = labelVoucher.labelSubsidyInfoColorType,
            labelBudgetsVoucher = labelVoucher.labelBudgetsVoucher.map {
                VoucherDetailData.LabelVoucher.LabelBudgetVoucher(
                    labelBudgetVoucherFormatted = it.labelBudgetVoucherFormatted,
                    labelBudgetVoucher = it.labelBudgetVoucher,
                    labelBudgetVoucherValue = it.labelBudgetVoucherValue
                )
            }
        )
    }

    private fun MerchantPromotionGetMVDataByIDResponse.MerchantPromotionGetMVDataByID.Data.toSubsidyDetail(): VoucherDetailData.SubsidyDetail {
        return VoucherDetailData.SubsidyDetail(
            programDetail = ProgramDetail(
                programName = subsidyDetail.programDetail.programName,
                programStatus = ProgramStatus.values().firstOrNull { value ->
                    value.id == subsidyDetail.programDetail.programStatus
                } ?: ProgramStatus.ONGOING,
                programLabel = subsidyDetail.programDetail.programLabel,
                programLabelDetail = subsidyDetail.programDetail.programLabelDetail,
                promotionStatus = PromotionStatus.values().firstOrNull { value ->
                    value.id == subsidyDetail.programDetail.promotionStatus
                } ?: PromotionStatus.REJECTED,
                promotionLabel = subsidyDetail.programDetail.promotionLabel
            ),
            quotaSubsidized = QuotaSubsidized(
                voucherQuota = subsidyDetail.quotaSubsidized.voucherQuota,
                remainingQuota = subsidyDetail.quotaSubsidized.remainingQuota,
                bookedGlobalQuota = subsidyDetail.quotaSubsidized.bookedGlobalQuota,
                confirmedGlobalQuota = subsidyDetail.quotaSubsidized.confirmedGlobalQuota
            )
        )
    }

    private fun Int.toBenefitType(): BenefitType {
        return when (this) {
            DiscountTypeConstant.NOMINAL -> BenefitType.NOMINAL
            DiscountTypeConstant.PERCENTAGE -> BenefitType.PERCENTAGE
            else -> BenefitType.NOMINAL
        }
    }
}
