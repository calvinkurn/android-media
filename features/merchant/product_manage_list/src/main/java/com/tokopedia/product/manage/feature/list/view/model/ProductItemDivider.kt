package com.tokopedia.product.manage.feature.list.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductMenuAdapterFactory

object ProductItemDivider: Visitable<ProductMenuAdapterFactory> {
    override fun type(typeFactory: ProductMenuAdapterFactory): Int {
        return typeFactory.type(this)
    }
}