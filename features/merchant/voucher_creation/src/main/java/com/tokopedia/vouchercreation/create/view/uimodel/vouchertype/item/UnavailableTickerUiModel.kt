package com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeItemTypeFactory

class UnavailableTickerUiModel(
        val onCloseTicker: () -> Unit
): Visitable<PromotionTypeItemTypeFactory> {

    override fun type(typeFactory: PromotionTypeItemTypeFactory): Int =
            typeFactory.type(this)
}