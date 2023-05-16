package com.tokopedia.inbox.universalinbox.data.response

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class UniversalInboxRecommendationWithTDN(
    val recommendationWidget: RecommendationWidget,
    val tdnBanner: List<TopAdsImageViewModel>
)
