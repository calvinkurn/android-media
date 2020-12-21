package com.tokopedia.productcard.options.divider

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.productcard.options.ProductCardOptionsTypeFactory

internal class ProductCardOptionsItemDivider: Visitable<ProductCardOptionsTypeFactory> {

    override fun type(typeFactory: ProductCardOptionsTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}