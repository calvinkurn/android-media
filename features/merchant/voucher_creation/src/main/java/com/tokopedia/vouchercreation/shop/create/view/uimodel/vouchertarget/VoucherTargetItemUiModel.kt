package com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget

import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherTargetCardType

data class VoucherTargetItemUiModel(
        var voucherTargetType: VoucherTargetCardType,
        var isEnabled: Boolean,
        var isHavePromoCard: Boolean,
        var promoCode: String = ""
)