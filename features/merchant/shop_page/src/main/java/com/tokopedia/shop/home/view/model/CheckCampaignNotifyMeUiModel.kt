package com.tokopedia.shop.home.view.model

data class CheckCampaignNotifyMeUiModel(
    val campaignId: String = "",
    val success: Boolean = false,
    val message: String = "",
    val errorMessage: String = "",
    val action: String = ""
)
