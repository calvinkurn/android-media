package com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.Productshimmer.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.categoryrevamp.data.typefactory.product.ProductTypeFactory

class BigListShimmerModel : Visitable<ProductTypeFactory> {
    override fun type(typeFactory: ProductTypeFactory): Int {
        return typeFactory.type(this)
    }
}