package com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch

data class InitialSearchUiModel(
        var id: String? = "",
        var hasMore: Boolean = false,
        var title: String? = "",
        var count: Int? = 0,
        var titleList: List<String> = listOf(),
        var sellerSearchList: List<ItemInitialSearchUiModel> = listOf()
)