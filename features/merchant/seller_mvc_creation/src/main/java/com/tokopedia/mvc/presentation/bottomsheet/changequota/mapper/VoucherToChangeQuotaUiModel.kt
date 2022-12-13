package com.tokopedia.mvc.presentation.bottomsheet.changequota.mapper

import com.tokopedia.mvc.presentation.bottomsheet.changequota.model.ChangeQuotaModel
import com.tokopedia.mvc.domain.entity.VoucherDetailData

object VoucherToChangeQuotaUiModel {
    fun VoucherDetailData.toChangeQuotaUiModel(): ChangeQuotaModel {
        return ChangeQuotaModel(
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
