package com.tokopedia.search.result.presentation.model

data class BroadMatchViewModel(
        val keyword: String = "",
        val url: String = "",
        val applink: String = "",
        val broadMatchItemViewModelList: List<BroadMatchItemViewModel> = listOf()
)