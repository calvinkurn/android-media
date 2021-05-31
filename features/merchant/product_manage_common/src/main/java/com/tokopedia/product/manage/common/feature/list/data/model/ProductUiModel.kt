package com.tokopedia.product.manage.common.feature.list.data.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.common.feature.list.view.adapter.factory.ProductManageAdapterFactory
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class ProductUiModel(
        val id: String,
        val title: String?,
        val imageUrl: String?,
        val minPrice: PriceUiModel?,
        val maxPrice: PriceUiModel?,
        val status: ProductStatus?,
        val url: String?,
        val cashBack: Int,
        val stock: Int?,
        val isFeatured: Boolean?,
        val isVariant: Boolean?,
        val multiSelectActive: Boolean,
        val isChecked: Boolean,
        val hasStockReserved: Boolean,
        val topAdsInfo: TopAdsInfo?,
        val access: ProductManageAccess?,
        val isCampaign: Boolean
) : Visitable<ProductManageAdapterFactory> {
    override fun type(typeFactory: ProductManageAdapterFactory): Int {
        return typeFactory.type(this)
    }

    fun isVariant(): Boolean = isVariant == true

    fun isActive(): Boolean = status == ProductStatus.ACTIVE
    fun isInactive(): Boolean = status == ProductStatus.INACTIVE
    fun isViolation(): Boolean = status == ProductStatus.VIOLATION
    fun isNotViolation(): Boolean = status != ProductStatus.VIOLATION
    fun isEmpty(): Boolean = status == ProductStatus.EMPTY || stock == 0
    fun hasTopAds(): Boolean = topAdsInfo?.isTopAds == true || topAdsInfo?.isAutoAds == true

    fun hasEditPriceAccess() = access?.editPrice == true
    fun hasEditProductAccess() = access?.editProduct == true
}