package com.tokopedia.shop.flashsale.presentation.detail

import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel

sealed class CancelCampaignActionResult {
    data class ActionAllowed(
        val campaign: CampaignUiModel,
    ) : CancelCampaignActionResult()

    data class RegisteredEventCampaign(val campaign: CampaignUiModel) : CancelCampaignActionResult()
}
