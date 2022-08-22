package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

@Deprecated(
    "Use VerticalSeparable instead",
    ReplaceWith(
        "VerticalSeparable",
        "com.tokopedia.search.result.product.separator.VerticalSeparable"
    )
)
class SeparatorDataView : Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }
}