package com.tokopedia.seller.search.feature.initialsearch.view.model.history

data class HistorySearchUiModel(
        var id: String? = "",
        var hasMore: Boolean = false,
        var title: String? = "",
        var historySearchList: List<ItemHistorySearchUiModel> = listOf()
)