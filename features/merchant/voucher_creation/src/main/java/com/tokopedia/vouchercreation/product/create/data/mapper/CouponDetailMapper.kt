package com.tokopedia.vouchercreation.product.create.data.mapper

import com.tokopedia.vouchercreation.product.create.data.response.Voucher
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import javax.inject.Inject

class CouponDetailMapper @Inject constructor() {

    fun map(merchantVoucherModel: Voucher): VoucherUiModel {
        return merchantVoucherModel.let {
            VoucherUiModel(
                id = it.voucherId,
                name = it.voucherName,
                type = it.voucherType,
                typeFormatted = it.voucherTypeFormatted,
                image = it.voucherImage,
                imageSquare = it.imageSquare,
                status = it.voucherStatus,
                discountTypeFormatted = it.discountTypeFormatted,
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
                isPublic = it.isPublic == 1,
                isVps = it.isVps == 1,
                packageName = it.packageName,
                isSubsidy = it.isSubsidy == 1,
                tnc = it.tnc,
                productIds = it.productIds
            )
        }
    }


}