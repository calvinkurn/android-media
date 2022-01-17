package com.tokopedia.search.result.presentation.model

data class SizeOptionDataView(
        val text: String = "",
        val img: String = "",
        val url: String = "",
        val hexColor: String = "",
        val applink: String = "",
        val filters: InspirationSizeOptionFiltersDataView = InspirationSizeOptionFiltersDataView(
                "",
                "",
                ""
        ),
        val inspirationCardType: String = ""
)
