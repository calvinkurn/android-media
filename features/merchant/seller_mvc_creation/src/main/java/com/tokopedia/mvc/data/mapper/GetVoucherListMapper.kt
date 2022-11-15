package com.tokopedia.mvc.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.mvc.data.response.MerchantVoucherModel
import com.tokopedia.mvc.domain.entity.Voucher
import javax.inject.Inject

class GetVoucherListMapper @Inject constructor() {

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
                tnc = it.tnc
            )
        }

}
