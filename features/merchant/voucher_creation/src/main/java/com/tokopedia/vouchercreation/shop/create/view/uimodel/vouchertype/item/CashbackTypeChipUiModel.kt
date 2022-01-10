package com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item

import com.tokopedia.vouchercreation.shop.create.view.enums.CashbackType

data class CashbackTypeChipUiModel(
        val cashbackType: CashbackType,
        var isActive: Boolean
)