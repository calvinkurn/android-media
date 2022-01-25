package com.tokopedia.search.result.presentation.model

data class InspirationData(
    val title: String = "",
    val type: String = "",
    val position: Int = 0,
    val optionCardData: List<InspirationCardOptionDataView> = listOf(),
    val optionSizeData: List<InspirationSizeOptionDataView> = listOf(),
)
