package com.tokopedia.mvc.data.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.response.MerchantVoucherModel
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.Voucher.*
import com.tokopedia.mvc.domain.entity.enums.ProgramStatus
import com.tokopedia.mvc.domain.entity.enums.PromotionStatus
import com.tokopedia.mvc.domain.entity.enums.SubsidyInfo
import com.tokopedia.mvc.domain.entity.enums.VoucherCreator
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import javax.inject.Inject

class GetVoucherListMapper @Inject constructor() {

    companion object {
        private const val DISCOUNT_TYPE_PERCENT = "percent"
        private const val VALUE_ACTIVE = 1
    }

    fun mapRemoteModelToUiModel(voucherList: List<MerchantVoucherModel>): List<Voucher> {
        return voucherList.map { mapRemoteModelToUiModel(it) }
    }

    fun mapRemoteModelToUiModel(merchantVoucherModel: MerchantVoucherModel): Voucher =
        merchantVoucherModel.let {
            Voucher(
                id = it.voucherId.toLongOrZero(),
                parentId = it.parentVoucherId.toLongOrZero(),
                name = it.voucherName,
                type = it.voucherType,
                typeFormatted = it.voucherTypeFormatted,
                image = it.voucherImage,
                imageSquare = it.imageSquare,
                imagePortrait = it.imagePortrait,
                status = VoucherStatus.values().firstOrNull { value ->
                    value.id == it.voucherStatus
                } ?: VoucherStatus.PROCESSING,
                discountUsingPercent = it.discountTypeFormatted == DISCOUNT_TYPE_PERCENT,
                discountAmt = it.discountAmt,
                discountAmtFormatted = it.discountAmtFormatted,
                discountAmtMax = it.discountAmtMax,
                minimumAmt = it.voucherMinimumAmt,
                quota = it.voucherQuota,
                confirmedQuota = it.confirmedQuota,
                bookedQuota = it.bookedQuota,
                startTime = it.startTime,
                finishTime = it.finishTime,
                code = it.voucherCode,
                createdTime = it.createTime,
                updatedTime = it.updateTime,
                isPublic = it.isPublic == VALUE_ACTIVE,
                isLockToProduct = it.isLockToProduct == VALUE_ACTIVE,
                isVps = it.isVps == VALUE_ACTIVE,
                totalChild = it.totalChild,
                packageName = it.packageName,
                isSubsidy = it.isSubsidy == VALUE_ACTIVE,
                tnc = it.tnc,
                targetBuyer = VoucherTargetBuyer.values().firstOrNull { value ->
                    value.id == it.targetBuyer
                } ?: VoucherTargetBuyer.ALL_BUYER,
                discountTypeFormatted = it.discountTypeFormatted,
                productIds = it.toProductIds(),
                isParent = it.isParent,
                labelVoucher = it.toLabelVoucher(),
                isEditable = it.isEditable,
                subsidyDetail = it.subsidyDetail.toProgramDetail()
            )
        }

    private fun MerchantVoucherModel.toLabelVoucher(): LabelVoucher {
        return LabelVoucher(
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
                LabelVoucher.LabelBudgetVoucher(
                    labelBudgetVoucher = it.labelBudgetVoucher,
                    labelBudgetVoucherValue = it.labelBudgetVoucherValue,
                    labelBudgetVoucherFormatted = it.labelBudgetVoucherFormatted
                )
            }
        )
    }

    private fun MerchantVoucherModel.SubsidyDetail.toProgramDetail(): SubsidyDetail {
        return SubsidyDetail(
            programDetail = SubsidyDetail.ProgramDetail(
                programName = programDetail.programName,
                programStatus = ProgramStatus.values().firstOrNull { value ->
                    value.id == programDetail.programStatus
                } ?: ProgramStatus.ONGOING,
                programLabel = programDetail.programLabel,
                programLabelDetail = programDetail.programLabelDetail,
                promotionStatus = PromotionStatus.values().firstOrNull { value ->
                    value.id == programDetail.promotionStatus
                } ?: PromotionStatus.REGISTERED,
                promotionLabel = programDetail.promotionLabel
            )
        )
    }

    private fun MerchantVoucherModel.toProductIds(): List<ProductId> {
        return productIds.map {
            ProductId(
                it.parentProductId,
                it.childProductId
            )
        }
    }
}
