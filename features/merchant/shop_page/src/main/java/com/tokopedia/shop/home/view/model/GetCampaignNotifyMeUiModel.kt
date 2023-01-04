package com.tokopedia.shop.home.view.model

data class GetCampaignNotifyMeUiModel(
    val campaignId: String = "",
    val success: Boolean = false,
    val message: String = "",
    val errorMessage: String = "",
    val isAvailable: Boolean = false
)
