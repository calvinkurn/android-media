package com.tokopedia.mvc.presentation.list.model

import com.tokopedia.mvc.domain.entity.enums.VoucherStatus

sealed class DeleteVoucherUiEffect {
    data class SuccessDeletedVoucher(
        val deleteVoucherId: Int = 0,
        val name : String= "",
        val voucherStatus: VoucherStatus = VoucherStatus.PROCESSING
    ): DeleteVoucherUiEffect()

    data class ShowToasterErrorDelete(
        val throwable: Throwable,
        val name : String= "",
        val voucherStatus: VoucherStatus = VoucherStatus.PROCESSING
    ): DeleteVoucherUiEffect()

    object OnProgressToDeletedVoucherList: DeleteVoucherUiEffect()

}
