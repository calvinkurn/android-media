package com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item

import com.tokopedia.vouchercreation.create.view.enums.CashbackType

data class CashbackTypeChipUiModel(
        val cashbackType: CashbackType,
        var isActive: Boolean
)