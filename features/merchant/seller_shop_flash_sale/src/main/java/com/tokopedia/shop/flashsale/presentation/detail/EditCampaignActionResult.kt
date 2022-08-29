package com.tokopedia.shop.flashsale.presentation.detail

sealed class EditCampaignActionResult {
    data class Allowed(
        val campaignId: Long,
    ) : EditCampaignActionResult()

    object RegisteredEventCampaign : EditCampaignActionResult()
}
