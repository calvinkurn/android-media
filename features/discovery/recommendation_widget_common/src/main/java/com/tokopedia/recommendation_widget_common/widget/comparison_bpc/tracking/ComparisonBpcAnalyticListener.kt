package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.tracking

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel

interface ComparisonBpcAnalyticListener {
    fun onProductCardImpressed(recommendationItem: RecommendationItem, trackingModel: RecommendationWidgetTrackingModel, anchorProductId: String, widgetTitle: String) { }
    fun onProductCardClicked(recommendationItem: RecommendationItem, trackingModel: RecommendationWidgetTrackingModel, anchorProductId: String) { }
    fun onViewAllCardClicked(trackingModel: RecommendationWidgetTrackingModel, productAnchorId: String) { }
}
