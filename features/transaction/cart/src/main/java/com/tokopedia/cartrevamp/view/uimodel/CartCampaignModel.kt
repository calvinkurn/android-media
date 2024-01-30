package com.tokopedia.cartrevamp.view.uimodel

data class CartCampaignModel(
    val logoUrl: String = "",
    val iconUrl: String = "",
    val text: String = "",
    val textColor: String = "",
    val lineColor: String = "",
    val backgroundColor: String = "",
    val backgroundGradientStartColor: String = "",
    val backgroundGradientEndColor: String = "",
    val endTimestamp: Long = 0,
)