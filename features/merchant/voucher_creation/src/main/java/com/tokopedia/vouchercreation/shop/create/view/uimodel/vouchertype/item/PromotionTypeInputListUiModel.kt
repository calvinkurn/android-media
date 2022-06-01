package com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.common.view.textfield.vouchertype.VoucherTextFieldUiModel
import com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertype.PromotionTypeItemTypeFactory

class PromotionTypeInputListUiModel(val inputList: List<VoucherTextFieldUiModel>) : Visitable<PromotionTypeItemTypeFactory> {

    override fun type(typeFactory: PromotionTypeItemTypeFactory): Int =
            typeFactory.type(this)
}