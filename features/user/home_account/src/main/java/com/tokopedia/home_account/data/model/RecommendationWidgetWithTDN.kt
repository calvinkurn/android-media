package com.tokopedia.home_account.data.model

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel

data class RecommendationWidgetWithTDN(
    val recommendationWidget: RecommendationWidget,
    val tdnBanner: TopAdsImageUiModel?
)
