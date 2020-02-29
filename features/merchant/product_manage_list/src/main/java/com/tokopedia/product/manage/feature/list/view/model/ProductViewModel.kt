package com.tokopedia.product.manage.feature.list.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class ProductViewModel(
    val id: String,
    val title: String?,
    val imageUrl: String?,
    val price: String?,
    val status: String?,
    val url: String?,
    val cashBack: Int,
    val stock: Int?,
    val isVariant: Boolean?
) : Visitable<BaseListCheckableTypeFactory<ProductViewModel>> {
    override fun type(typeFactory: BaseListCheckableTypeFactory<ProductViewModel>): Int {
        return typeFactory.type(this)
    }

    fun isVariant(): Boolean = isVariant == true
    fun isStockEmpty(): Boolean = stock == 0

    fun isActive(): Boolean = status == ProductStatus.ACTIVE.name
    fun isInactive(): Boolean = status == ProductStatus.INACTIVE.name
    fun isBanned(): Boolean = status == ProductStatus.BANNED.name
}