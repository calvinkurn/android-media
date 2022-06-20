package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign

import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign.adapter.RelatedCampaignItem

sealed class ChooseRelatedCampaignResult {
    object Loading : ChooseRelatedCampaignResult()
    object SearchEmptyResult : ChooseRelatedCampaignResult()
    data class Success(
        val keyword: String,
        val relatedCampaigns: List<RelatedCampaignItem>,
    ) : ChooseRelatedCampaignResult()

    data class Fail(val error: Throwable) : ChooseRelatedCampaignResult()
}
