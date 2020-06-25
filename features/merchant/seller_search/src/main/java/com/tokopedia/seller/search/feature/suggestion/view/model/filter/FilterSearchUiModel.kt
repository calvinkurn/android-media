package com.tokopedia.seller.search.feature.suggestion.view.model.filter

data class FilterSearchUiModel(
        val keyword: String? = "",
        val title: String? = "",
        val isSelected: Boolean = false
)