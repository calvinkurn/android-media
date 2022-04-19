package com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertype.PromotionTypeItemTypeFactory

class VoucherTitleUiModel(
        val title: String
): Visitable<PromotionTypeItemTypeFactory> {

    override fun type(typeFactory: PromotionTypeItemTypeFactory): Int =
            typeFactory.type(this)
}