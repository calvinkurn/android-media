package com.tokopedia.seller.search.feature.initialsearch.view.model.product

data class ProductSearchUiModel(
        var id: String? = "",
        var hasMore: Boolean = false,
        var title: String? = "",
        var productSearchList: List<ItemProductSearchUiModel> = listOf()
)