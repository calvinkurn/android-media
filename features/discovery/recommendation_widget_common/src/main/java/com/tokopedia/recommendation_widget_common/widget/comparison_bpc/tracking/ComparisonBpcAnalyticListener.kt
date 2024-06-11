package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.tracking

import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model.ComparisonBpcSeeMoreDataModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel

interface ComparisonBpcAnalyticListener {
    fun onProductCardImpressed(recommendationItem: RecommendationItem, trackingModel: RecommendationWidgetTrackingModel, anchorProductId: String, widgetTitle: String) { }
    fun onProductCardByteIoView(recommendationItem: RecommendationItem, appLogAdditionalParam: AppLogAdditionalParam) { }
    fun onProductCardClicked(recommendationItem: RecommendationItem, trackingModel: RecommendationWidgetTrackingModel, anchorProductId: String, appLogAdditionalParam: AppLogAdditionalParam) { }
    fun onViewAllCardClicked(element: ComparisonBpcSeeMoreDataModel) { }
}
