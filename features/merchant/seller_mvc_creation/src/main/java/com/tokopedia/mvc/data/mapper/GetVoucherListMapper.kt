package com.tokopedia.mvc.data.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.response.MerchantVoucherModel
import com.tokopedia.mvc.domain.entity.Voucher
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
                isParent = it.isParent
            )
        }

    private fun MerchantVoucherModel.toProductIds(): List<Voucher.ProductId> {
        return productIds.map {
            Voucher.ProductId(
                it.parentProductId,
                it.childProductId
            )
        }
    }
}
