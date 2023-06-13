package com.tokopedia.shop.home.view.model

import com.tokopedia.shop.common.data.model.ShopPageWidgetLayoutUiModel

data class ShopPageHomeWidgetLayoutUiModel(
    val layoutId: String = "",
    val masterLayoutId: String = "",
    val listWidgetLayout: List<ShopPageWidgetLayoutUiModel> = listOf()
)
