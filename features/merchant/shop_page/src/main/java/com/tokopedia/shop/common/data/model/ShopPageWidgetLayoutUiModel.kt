package com.tokopedia.shop.common.data.model

data class ShopPageWidgetLayoutUiModel(
    val widgetId: String = "",
    val widgetMasterId: String = "",
    val widgetTitle: String = "",
    val widgetType: String = "",
    val widgetName: String = "",
    val isFestivity: Boolean = false,
    val header: ShopPageHeaderUiModel = ShopPageHeaderUiModel()
)
