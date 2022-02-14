package com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.MvcLockedToProductTypeFactory

class MvcLockedToProductGridProductUiModel(
    val productID: String = "",
    val finalPrice: String = "",
    val productCardModel: ProductCardModel = ProductCardModel()
) : Visitable<MvcLockedToProductTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: MvcLockedToProductTypeFactory): Int {
        return typeFactory.type(this)
    }
}