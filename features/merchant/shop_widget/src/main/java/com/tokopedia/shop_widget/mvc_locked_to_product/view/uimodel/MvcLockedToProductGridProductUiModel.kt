package com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.MvcLockedToProductTypeFactory

class MvcLockedToProductGridProductUiModel(
    val productID: String = "",
    val childIDs: List<String> = listOf(),
    val city: String = "",
    val minimumOrder: Int = 0,
    val stock: Int = 0,
    val productInCart: Int = 0,
    val isVariant: Boolean = false,
    var productCardModel: ProductCardModel = ProductCardModel()
) : Visitable<MvcLockedToProductTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: MvcLockedToProductTypeFactory): Int {
        return typeFactory.type(this)
    }
}