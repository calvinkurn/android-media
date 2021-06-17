package com.tokopedia.shop.score.penalty.domain.response

data class ShopPenaltySummaryTypeWrapper(
        var shopScorePenaltySummaryResponse: ShopScorePenaltySummaryResponse.ShopScorePenaltySummary? = null,
        var shopScorePenaltyTypesResponse: ShopScorePenaltyTypesResponse.ShopScorePenaltyTypes? = null
)