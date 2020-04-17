package com.tokopedia.product.manage.feature.quickedit.variant.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory.ProductVariantAdapterFactory

data class ProductVariant(
    val id: String,
    val name: String,
    val price: Int
): Visitable<ProductVariantAdapterFactory> {

    override fun type(typeFactory: ProductVariantAdapterFactory): Int {
        return typeFactory.type(this)
    }
}