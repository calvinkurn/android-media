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
                    shopId = it.shopId,
                    name = it.voucherName,
                    type = it.voucherType,
                    typeFormatted = it.voucherTypeFormatted,
                    image = it.voucherImage,
                    imageSquare = it.imageSquare,
                    status = it.voucherStatus,
                    statusFormatted = it.voucherStatusFormatted,
                    discountType = it.discountType,
                    discountTypeFormatted = it.discountTypeFormatted,
                    discountAmt = it.discountAmt,
                    discountAmtFormatted = it.discountAmtFormatted,
                    discountAmtMax = it.discountAmtMax,
                    discountAmtMaxFormatted = it.discountAmtMaxFormatted,
                    minimumAmt = it.voucherMinimumAmt,
                    minimumAmtFormatted = it.voucherMinimumAmtFormatted,
                    quota = it.voucherQuota,
                    remainingQuota = it.remainingQuota,
                    bookedQuota = it.bookedQuota,
                    startTime = it.startTime,
                    finishTime = it.finishTime,
                    code = it.voucherCode,
                    galadrielVoucherId = it.galadrielVoucherId,
                    galadrielCatalogId = it.galadrielCatalogId,
                    createdTime = it.createTime,
                    createdBy = it.createBy,
                    updatedTime = it.updateTime,
                    updatedBy = it.updateBy,
                    isPublic = it.isPublic == 1,
                    isQuotaAvailable = it.isQuotaAvailable == 1,
                    tnc = it.tnc
            )
        }
    }

    fun mapRemoteModelToUiModel(merchantVoucherModel: MerchantVoucherModel): VoucherUiModel =
        merchantVoucherModel.let {
            VoucherUiModel(
                    id = it.voucherId,
                    shopId = it.shopId,
                    name = it.voucherName,
                    type = it.voucherType,
                    typeFormatted = it.voucherTypeFormatted,
                    image = it.voucherImage,
                    imageSquare = it.imageSquare,
                    status = it.voucherStatus,
                    statusFormatted = it.voucherStatusFormatted,
                    discountType = it.discountType,
                    discountTypeFormatted = it.discountTypeFormatted,
                    discountAmt = it.discountAmt,
                    discountAmtFormatted = it.discountAmtFormatted,
                    discountAmtMax = it.discountAmtMax,
                    discountAmtMaxFormatted = it.discountAmtMaxFormatted,
                    minimumAmt = it.voucherMinimumAmt,
                    minimumAmtFormatted = it.voucherMinimumAmtFormatted,
                    quota = it.voucherQuota,
                    remainingQuota = it.remainingQuota,
                    bookedQuota = it.bookedQuota,
                    startTime = it.startTime,
                    finishTime = it.finishTime,
                    code = it.voucherCode,
                    galadrielVoucherId = it.galadrielVoucherId,
                    galadrielCatalogId = it.galadrielCatalogId,
                    createdTime = it.createTime,
                    createdBy = it.createBy,
                    updatedTime = it.updateTime,
                    updatedBy = it.updateBy,
                    isPublic = it.isPublic == 1,
                    isQuotaAvailable = it.isQuotaAvailable == 1,
                    tnc = it.tnc
            )
        }

}