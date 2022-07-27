package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign

import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign

interface ChooseRelatedCampaignBottomSheetListener {
    fun onRelatedCampaignsAddButtonClicked(relatedCampaigns: List<RelatedCampaign>)
}