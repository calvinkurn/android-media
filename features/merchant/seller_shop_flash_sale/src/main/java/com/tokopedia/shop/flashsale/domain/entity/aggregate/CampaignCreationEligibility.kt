package com.tokopedia.shop.flashsale.domain.entity.aggregate

data class CampaignCreationEligibility(
    val remainingQuota: Int,
    val isEligible: Boolean
)