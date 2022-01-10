package com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.shop.create.view.enums.CashbackType
import com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertype.PromotionTypeItemTypeFactory

class CashbackTypePickerUiModel(val onSelectedType: (CashbackType) -> Unit = {},
                                var currentActiveType: CashbackType = CashbackType.Rupiah) : Visitable<PromotionTypeItemTypeFactory> {

    override fun type(typeFactory: PromotionTypeItemTypeFactory): Int =
            typeFactory.type(this)
}