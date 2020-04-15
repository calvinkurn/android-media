package com.tokopedia.common_category.model.shimmer

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common_category.factory.ProductTypeFactory

class ListShimmerModel : Visitable<com.tokopedia.common_category.factory.ProductTypeFactory> {
    override fun type(typeFactory: com.tokopedia.common_category.factory.ProductTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}