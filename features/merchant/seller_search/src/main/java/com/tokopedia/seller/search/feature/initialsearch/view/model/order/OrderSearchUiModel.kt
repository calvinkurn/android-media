package com.tokopedia.seller.search.feature.initialsearch.view.model.order

data class OrderSearchUiModel(
        var id: String? = "",
        var hasMore: Boolean = false,
        var title: String? = "",
        var orderSearchList: List<ItemOrderSearchUiModel> = listOf()
)