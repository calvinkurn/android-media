package com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch

data class InitialSearchUiModel(
        val id: String? = "",
        val hasMore: Boolean = false,
        val title: String? = "",
        val count: Int? = 0,
        val titleList: List<String> = listOf(),
        val sellerSearchList: List<ItemInitialSearchUiModel> = listOf()
)