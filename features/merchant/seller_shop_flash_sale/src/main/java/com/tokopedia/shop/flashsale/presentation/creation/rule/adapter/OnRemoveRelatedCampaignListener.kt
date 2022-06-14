package com.tokopedia.shop.flashsale.presentation.creation.rule.adapter

import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign

interface OnRemoveRelatedCampaignListener {
    fun onRelatedCampaignRemoved(relatedCampaign: RelatedCampaign)
}