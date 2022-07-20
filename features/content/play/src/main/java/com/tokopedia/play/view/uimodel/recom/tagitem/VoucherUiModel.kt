package com.tokopedia.play.view.uimodel.recom.tagitem

import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel

data class VoucherUiModel(
    val voucherList: List<MerchantVoucherUiModel>,
) {
    companion object {
        val Empty: VoucherUiModel
            get() = VoucherUiModel(
                voucherList = emptyList(),
            )
    }
}