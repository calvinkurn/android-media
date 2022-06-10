package com.tokopedia.shop.flashsale.presentation.creation.rule.adapter

import com.tokopedia.shop.flashsale.presentation.creation.rule.RelatedCampaign

interface OnRemoveRelatedCampaignListener {
    fun onRelatedCampaignRemoved(relatedCampaign: RelatedCampaign)
}