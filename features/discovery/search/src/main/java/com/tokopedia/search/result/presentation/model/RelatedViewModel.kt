package com.tokopedia.search.result.presentation.model

data class RelatedViewModel(
        val relatedKeyword: String = "",
        val position: Int = 0,
        val broadMatchViewModelList: List<BroadMatchViewModel> = listOf()
)