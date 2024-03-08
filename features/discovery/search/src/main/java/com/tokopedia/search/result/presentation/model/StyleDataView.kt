package com.tokopedia.search.result.presentation.model

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductV5

data class StyleDataView(
    val key: String = "",
    val value: String = "",
) {
    companion object {
        fun create(style: SearchProductV5.Data.Style): StyleDataView =
            StyleDataView(style.key, style.value)

        fun create(style: com.tokopedia.topads.sdk.domain.model.Style): StyleDataView =
            StyleDataView(style.key, style.value)

        fun create(style: SearchProductModel.Style): StyleDataView =
            StyleDataView(style.key, style.value)
    }
}
