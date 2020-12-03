package com.tokopedia.shop.product.view.datamodel

data class GetShopProductUiModel(
        var hasNextPage: Boolean = false,
        var listShopProductUiModel: List<ShopProductUiModel> = listOf(),
        var totalProductData: Int = 0
)
