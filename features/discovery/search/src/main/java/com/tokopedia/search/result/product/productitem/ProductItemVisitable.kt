package com.tokopedia.search.result.product.productitem

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

interface ProductItemVisitable: Visitable<ProductListTypeFactory> {

    val hasLabelGroupFulfillment: Boolean

    fun isCountedAsProductItem(): Boolean = true
}
