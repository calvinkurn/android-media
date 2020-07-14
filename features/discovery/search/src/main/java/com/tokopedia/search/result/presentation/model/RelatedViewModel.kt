package com.tokopedia.search.result.presentation.model

data class RelatedViewModel(
        val relatedKeyword: String = "",
        val broadMatchViewModelList: List<BroadMatchViewModel> = listOf()
)