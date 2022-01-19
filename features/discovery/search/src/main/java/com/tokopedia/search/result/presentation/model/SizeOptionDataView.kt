package com.tokopedia.search.result.presentation.model

import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking

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
        val inspirationCardType: String = "",
        val componentId: String = "",
        val keyword: String = "",
        val valueName: String = "",
        val dimension90: String = ""
) : SearchComponentTracking by searchComponentTracking(
        componentId = componentId,
        valueName = valueName,
        valueId = VALUE_ID,
        keyword = keyword,
        dimension90 = dimension90
) {
        companion object {
                const val VALUE_ID = "0"
        }
}
