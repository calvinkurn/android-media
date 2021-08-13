package com.tokopedia.search.result.presentation.model

data class RelatedDataView(
        val relatedKeyword: String = "",
        val position: Int = 0,
        val broadMatchDataViewList: List<BroadMatchDataView> = listOf()
)