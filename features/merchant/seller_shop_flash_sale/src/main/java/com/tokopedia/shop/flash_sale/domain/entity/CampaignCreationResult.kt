package com.tokopedia.shop.flash_sale.domain.entity

data class CampaignCreationResult(
    val campaignId: Long,
    val isSuccess: Boolean,
    val totalProductFailed: Int,
    val errorDescription: String,
    val errorTitle: String
)