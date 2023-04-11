package com.tokopedia.play.view.uimodel.recom.tagitem

import com.tokopedia.play.view.uimodel.PlayVoucherUiModel

data class VoucherUiModel(
    val voucherList: List<PlayVoucherUiModel>,
) {
    companion object {
        val Empty: VoucherUiModel
            get() = VoucherUiModel(
                voucherList = emptyList(),
            )
    }
}