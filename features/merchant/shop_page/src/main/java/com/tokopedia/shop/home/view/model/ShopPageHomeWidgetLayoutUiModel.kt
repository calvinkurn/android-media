package com.tokopedia.shop.home.view.model

data class ShopPageHomeWidgetLayoutUiModel(
        val layoutId: String = "",
        val masterLayoutId: String = "",
        val listWidgetLayout: List<WidgetLayout> = listOf()
) {
    data class WidgetLayout(
            val widgetId: String = "",
            val widgetMasterId: String = "",
            val widgetType: String = "",
            val widgetName: String = ""
    )
}