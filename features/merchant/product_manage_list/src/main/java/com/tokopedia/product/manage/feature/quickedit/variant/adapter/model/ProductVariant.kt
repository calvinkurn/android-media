package com.tokopedia.product.manage.feature.quickedit.variant.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory.ProductVariantAdapterFactory
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Picture
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class ProductVariant(
    val id: String,
    val name: String,
    val status: ProductStatus,
    val combination: List<Int>,
    val isPrimary: Boolean,
    val price: Int,
    val sku: String,
    val stock: Int,
    val pictures: List<Picture>
): Visitable<ProductVariantAdapterFactory> {

    override fun type(typeFactory: ProductVariantAdapterFactory): Int {
        return typeFactory.type(this)
    }
}