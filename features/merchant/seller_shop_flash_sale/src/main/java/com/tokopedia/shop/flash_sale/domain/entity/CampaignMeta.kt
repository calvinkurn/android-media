package com.tokopedia.shop.flash_sale.domain.entity

data class CampaignMeta(
    val totalCampaign : Int,
    val totalCampaignActive: Int,
    val totalCampaignFinished : Int,
    val campaigns : List<CampaignUiModel>
)
