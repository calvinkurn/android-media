package com.tokopedia.cartrevamp.view.uimodel

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

data class CartRecommendationItemHolderData(
    var hasSentImpressionAnalytics: Boolean = false,
    var recommendationItem: RecommendationItem
)
