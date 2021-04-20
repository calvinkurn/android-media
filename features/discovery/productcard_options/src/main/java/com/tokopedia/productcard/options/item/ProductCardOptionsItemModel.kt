package com.tokopedia.productcard.options.item

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.productcard.options.ProductCardOptionsTypeFactory

internal data class ProductCardOptionsItemModel(
        val title: String = "",
        val onClick: () -> Unit = { }
): Visitable<ProductCardOptionsTypeFactory> {

    override fun type(typeFactory: ProductCardOptionsTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}