package com.tokopedia.common_category.model.shimmer

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common_category.factory.ProductTypeFactory

class BigListShimmerModel : Visitable<ProductTypeFactory> {
    override fun type(typeFactory: ProductTypeFactory): Int {
        return typeFactory.type(this)
    }
}