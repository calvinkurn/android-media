package com.tokopedia.shop.flashsale.domain.entity

data class CampaignMeta(
    val totalCampaign : Int,
    val totalCampaignActive: Int,
    val totalCampaignFinished : Int,
    val campaigns : List<CampaignUiModel>
)
