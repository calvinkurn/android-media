package com.tokopedia.vouchercreation.common.model

import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 11/05/20
 */

class VoucherMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(voucherList: List<MerchantVoucherModel>): List<VoucherUiModel> {
        return voucherList.map {
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
                    isPublic = it.isPublic == 1
            )
        }
    }

    fun mapRemoteModelToUiModel(merchantVoucherModel: MerchantVoucherModel): VoucherUiModel =
        merchantVoucherModel.let {
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
                    isPublic = it.isPublic == 1
            )
        }

}