package com.tokopedia.vouchercreation.create.view.uimodel

data class VoucherTargetItemUiModel(
        var voucherTargetType: Int,
        var isEnabled: Boolean,
        var isHavePromoCard: Boolean,
        var promoCode: String = ""
)