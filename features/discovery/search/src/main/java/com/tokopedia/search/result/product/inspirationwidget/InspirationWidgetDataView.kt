package com.tokopedia.search.result.product.inspirationwidget

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationWidgetData

data class InspirationWidgetDataView(
    val title: String = "",
    val headerTitle: String = "",
    val headerSubtitle: String = "",
    val layout: String = "",
    val type: String = "",
    val position: Int = 0,
) {
    companion object {

        fun create(data: InspirationWidgetData): InspirationWidgetDataView {
            return InspirationWidgetDataView(
                title = data.title,
                headerTitle = data.headerTitle,
                headerSubtitle = data.headerSubtitle,
                layout = data.layout,
                type = data.type,
                position = data.position,
            )
        }
    }
}
