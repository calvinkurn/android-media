package com.tokopedia.shop.common.data.model

import com.tokopedia.shop.home.data.model.ShopPageWidgetRequestModel

data class ShopPageWidgetUiModel(
    val widgetId: String = "",
    val widgetMasterId: String = "",
    val widgetTitle: String = "",
    val widgetType: String = "",
    val widgetName: String = "",
    val isFestivity: Boolean = false,
    val header: ShopPageHeaderUiModel = ShopPageHeaderUiModel(),
    val options: List<ShopPageWidgetRequestModel.Option> = emptyList()
)
