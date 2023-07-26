package com.tokopedia.shop.home.view.model

import com.tokopedia.shop.common.data.model.ShopPageWidgetUiModel

data class ShopPageLayoutUiModel(
    val layoutId: String = "",
    val masterLayoutId: String = "",
    //TODO need to change this model to BaseShopHomeWidgetUiModel in the future
    val listWidgetLayout: List<ShopPageWidgetUiModel> = listOf()
)
