package com.tokopedia.cart.view.uimodel

import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata

/**
 * Created by Irfan Khoirul on 2019-05-31.
 */

data class CartRecentViewHolderData(
    var hasSentImpressionAnalytics: Boolean = false,
    val recommendationWidgetMetadata: RecommendationWidgetMetadata = RecommendationWidgetMetadata(),
)
