package com.tokopedia.shop.common.data.model

data class ShopPageHeaderUiModel(
    val title: String = "",
    val subtitle: String = "",
    val ctaText: String = "",
    val ctaLink: String = "",
    val isAtc: Int = -1,
    val etalaseId: String = "",
    val isShowEtalaseName: Int = -1,
    val data: List<ShopPageHeaderDataUiModel> = listOf()
)
