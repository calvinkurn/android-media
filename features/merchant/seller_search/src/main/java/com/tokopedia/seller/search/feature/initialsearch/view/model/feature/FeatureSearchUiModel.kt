package com.tokopedia.seller.search.feature.initialsearch.view.model.feature

data class FeatureSearchUiModel(
        var id: String? = "",
        var hasMore: Boolean = false,
        var title: String? = "",
        var featureSearchList: List<ItemFeatureSearchUiModel> = listOf()
)