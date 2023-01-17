package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created by @ilhamsuaib on 28/11/22.
 */

data class WidgetLayoutUiModel(
    val widgetList: List<BaseWidgetUiModel<*>> = emptyList(),
    val shopState: ShopStateUiModel = ShopStateUiModel.None
)