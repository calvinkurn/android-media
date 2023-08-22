package com.tokopedia.shop_widget.common.uimodel

import com.tokopedia.shop.common.view.model.ShopPageColorSchema

data class DynamicHeaderUiModel(
    val title: String = "",
    val subTitle: String = "",
    val ctaText: String = "",
    val ctaTextLink: String = "",
    val statusCampaign: String = "",
    val endDate: String = "",
    val timerCounter: String = "",
    val isOverrideTheme: Boolean,
    val colorSchema: ShopPageColorSchema,
)
