package com.tokopedia.product.manage.feature.list.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class ProductViewModel(
    val id: String,
    val title: String?,
    val imageUrl: String?,
    val price: String?,
    val priceFormatted: String?,
    val status: ProductStatus?,
    val url: String?,
    val cashBack: Int,
    val stock: Int?,
    val isFeatured: Boolean?,
    val isVariant: Boolean?,
    val multiSelectActive: Boolean
) : Visitable<BaseListCheckableTypeFactory<ProductViewModel>> {
    override fun type(typeFactory: BaseListCheckableTypeFactory<ProductViewModel>): Int {
        return typeFactory.type(this)
    }

    fun isVariant(): Boolean = isVariant == true
    fun isNotVariant(): Boolean = isVariant != true
    fun isFeatured(): Boolean = isFeatured == true

    fun isActive(): Boolean = status == ProductStatus.ACTIVE
    fun isInactive(): Boolean = status == ProductStatus.INACTIVE
    fun isViolation(): Boolean = status == ProductStatus.VIOLATION
    fun isEmpty(): Boolean = status == ProductStatus.EMPTY
}