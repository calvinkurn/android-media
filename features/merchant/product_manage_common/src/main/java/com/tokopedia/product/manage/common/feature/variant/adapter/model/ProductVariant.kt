package com.tokopedia.product.manage.common.feature.variant.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.variant.adapter.factory.ProductVariantAdapterFactory
import com.tokopedia.product.manage.common.feature.variant.data.model.Picture
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class ProductVariant(
        val id: String,
        val name: String,
        val status: ProductStatus,
        val combination: List<Int>,
        val isPrimary: Boolean,
        val isCampaign: Boolean,
        val price: Int,
        val sku: String,
        val stock: Int,
        val pictures: List<Picture>,
        val isAllStockEmpty: Boolean = false,
        val access: ProductManageAccess
): Visitable<ProductVariantAdapterFactory> {

    override fun type(typeFactory: ProductVariantAdapterFactory): Int {
        return typeFactory.type(this)
    }

    fun isActive(): Boolean {
        return status == ProductStatus.ACTIVE
    }

    fun isInactive(): Boolean {
        return status == ProductStatus.INACTIVE
    }

    fun isEmpty(): Boolean {
        return stock == 0
    }
}