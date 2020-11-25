package com.tokopedia.shop.home.view.model

data class GetShopHomeProductUiModel(
        var hasNextPage: Boolean = false,
        var listShopProductUiModel: List<ShopHomeProductUiModel> = listOf(),
        var totalProductData: Int = 0
)
