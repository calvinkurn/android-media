package com.tokopedia.shop.flashsale.presentation.creation.rule

import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign

data class AddRelatedCampaignRequest(
    val campaignId: Long,
    val selectedRelatedCampaign: List<RelatedCampaign>
)