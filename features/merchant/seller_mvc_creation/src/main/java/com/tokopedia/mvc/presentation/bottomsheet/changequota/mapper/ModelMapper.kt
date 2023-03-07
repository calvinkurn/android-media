package com.tokopedia.mvc.presentation.bottomsheet.changequota.mapper

import com.tokopedia.mvc.presentation.bottomsheet.changequota.model.UpdateQuotaModel
import com.tokopedia.mvc.domain.entity.VoucherDetailData

object ModelMapper {
    fun VoucherDetailData.toUpdateQuotaModelMapper(): UpdateQuotaModel {
        return UpdateQuotaModel(
            voucherId= this.voucherId,
            voucherName= this.voucherName,
            currentQuota= voucherQuota,
            maxBenefit= voucherDiscountAmountMax,
            isMultiPeriod= isPeriod,
            isVoucherProduct= this.isVoucherProduct,
            voucherType= this.voucherType,
            voucherStatus = this.voucherStatus
        )
    }
}
