package com.tokopedia.mvc.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.mvc.data.response.MerchantVoucherModel
import com.tokopedia.mvc.domain.entity.Voucher
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
                id = it.voucherId.toIntOrZero(),
                name = it.voucherName,
                type = it.voucherType,
                typeFormatted = it.voucherTypeFormatted,
                image = it.voucherImage,
                imageSquare = it.imageSquare,
                imagePortrait = it.imagePortrait,
                status = it.voucherStatus,
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
                targetBuyer = it.targetBuyer
            )
        }

}
