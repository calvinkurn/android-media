package com.tokopedia.vouchercreation.create.view.uimodel.vouchertarget.reload

import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType

data class VoucherTargetReloadUiModel (
        @VoucherTargetType val type: Int,
        val voucherName: String,
        val promoCode: String
)