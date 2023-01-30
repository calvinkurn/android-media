package com.tokopedia.shop.home.view.model

data class ShopPageHomeLayoutUiModel(
    val layoutId: String = "",
    val masterLayoutId: Int = -1,
    val merchantTierId: Int = -1,
    val status: Int = -1,
    val maxWidgets: Int = -1,
    val publishDate: String = "",
    val listWidget: List<BaseShopHomeWidgetUiModel> = listOf()
)
