package com.tokopedia.search.result.mps.shopwidget

import com.tokopedia.search.result.mps.domain.model.MPSModel.AceSearchShop.Shop.Product

data class MPSShopWidgetProductDataView(
    val productId: String = "",
    val productImageUrl: String = "",
    val productName: String = "",
    val applink: String = "",
    val originalPrice: String = "",
    val discountPercentage: String = "",
    val priceFormat: String = "",
    val ratingAverage: String = "",
    val labelGroupList: List<MPSProductLabelGroupDataView> = listOf(),
) {

    companion object {
        fun create(product: Product) = MPSShopWidgetProductDataView(
            productId = product.id,
            productImageUrl = product.imageUrl,
            productName = product.name,
            applink = product.applink,
            originalPrice = product.originalPrice,
            discountPercentage = product.discountPercentage,
            priceFormat = product.priceFormat,
            ratingAverage = product.ratingAverage,
            labelGroupList = product.labelGroupList.map(MPSProductLabelGroupDataView::create)
        )
    }
}
