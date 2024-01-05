package com.tokopedia.search.result.presentation.model

import com.tokopedia.search.result.domain.model.SearchProductV5

data class StyleDataView(
    val key: String = "",
    val value: String = "",
) {
    companion object {
        fun create(style: List<SearchProductV5.Data.Style>): List<StyleDataView> {
            return style.map { item ->
                StyleDataView(
                    item.key,
                    item.value
                )
            }
        }
    }
}
