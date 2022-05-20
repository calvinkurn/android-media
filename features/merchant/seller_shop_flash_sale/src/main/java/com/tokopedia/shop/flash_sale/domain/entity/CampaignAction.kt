package com.tokopedia.shop.flash_sale.domain.entity

sealed class CampaignAction {
    object Create : CampaignAction()
    data class Update(val campaignId : Long) : CampaignAction()
    data class Submit(val campaignId : Long) : CampaignAction()
}