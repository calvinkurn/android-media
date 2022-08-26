package com.tokopedia.shop.flashsale.domain.entity

sealed class CampaignAction {
    object Create : CampaignAction()
    data class Update(val campaignId : Long) : CampaignAction()
    data class Submit(val campaignId : Long) : CampaignAction()
}