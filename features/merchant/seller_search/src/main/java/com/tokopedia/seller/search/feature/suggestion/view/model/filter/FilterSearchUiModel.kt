package com.tokopedia.seller.search.feature.suggestion.view.model.filter

data class FilterSearchUiModel(
        var keyword: String? = "",
        var title: String? = "",
        var isSelected: Boolean = false
)