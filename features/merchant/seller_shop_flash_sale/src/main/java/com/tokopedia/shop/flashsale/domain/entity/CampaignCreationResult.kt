package com.tokopedia.shop.flashsale.domain.entity

data class CampaignCreationResult(
    val campaignId: Long,
    val isSuccess: Boolean,
    val totalProductFailed: Int,
    val errorDescription: String,
    val errorTitle: String,
    val errorMessage: String
)