package com.tokopedia.product.manage.feature.quickedit.variant.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory.ProductVariantAdapterFactory

object ProductTicker: Visitable<ProductVariantAdapterFactory> {
    override fun type(typeFactory: ProductVariantAdapterFactory): Int {
       return typeFactory.type(this)
    }
}