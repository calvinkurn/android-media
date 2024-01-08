package com.tokopedia.search.result.presentation.model

import com.tokopedia.search.result.domain.model.SearchProductV5

data class StyleDataView(
    val key: String = "",
    val value: String = "",
) {
    companion object {
        fun create(style: SearchProductV5.Data.Style): StyleDataView =
            StyleDataView(style.key, style.value)
    }
}
