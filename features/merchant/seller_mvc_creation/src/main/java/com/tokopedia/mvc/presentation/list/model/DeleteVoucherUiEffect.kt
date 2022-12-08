package com.tokopedia.mvc.presentation.list.model

import com.tokopedia.mvc.domain.entity.enums.VoucherStatus

sealed class DeleteVoucherUiEffect {
    data class SuccessDeletedVoucher(
        val deleteVoucherId: Int? = null,
        val name : String?= null,
        val voucherStatus: VoucherStatus = VoucherStatus.PROCESSING
    ): DeleteVoucherUiEffect()

    data class ShowToasterErrorDelete(
        val throwable: Throwable,
        val name : String?= null,
        val voucherStatus: VoucherStatus = VoucherStatus.PROCESSING
    ): DeleteVoucherUiEffect()

    object OnProgressToDeletedVoucherList: DeleteVoucherUiEffect()

}
