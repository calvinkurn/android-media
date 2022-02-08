package com.tokopedia.shop_widget.common.uimodel

data class DynamicHeaderUiModel(
    val title: String,
    val subTitle: String,
    val ctaText: String,
    val ctaTextLink: String,
    val startDate: String,
    val endDate: String,
    val timeCounter: Long,
    val statusCampaign: String
)