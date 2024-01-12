package com.tokopedia.search.result.product.separator

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

object VerticalSeparatorDataView: Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory?): Int =
        typeFactory?.type(this) ?: 0
}
