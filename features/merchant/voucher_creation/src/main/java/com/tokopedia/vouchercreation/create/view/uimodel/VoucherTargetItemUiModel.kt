package com.tokopedia.vouchercreation.create.view.uimodel

import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType

data class VoucherTargetItemUiModel(
        var voucherTargetType: VoucherTargetCardType,
        var isEnabled: Boolean,
        var isHavePromoCard: Boolean,
        var promoCode: String = ""
)