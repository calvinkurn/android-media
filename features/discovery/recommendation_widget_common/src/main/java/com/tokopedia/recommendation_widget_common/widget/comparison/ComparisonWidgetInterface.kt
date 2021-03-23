package com.tokopedia.recommendation_widget_common.widget.comparison

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface ComparisonWidgetScrollInterface {
    fun scrollX(x: Int)
}

interface ComparisonWidgetInterface{
    fun onProductCardImpressed(recommendationItem: RecommendationItem, comparisonListModel: ComparisonListModel, position: Int)
    fun onProductCardClicked(recommendationItem: RecommendationItem, comparisonListModel: ComparisonListModel, position: Int)
    fun onSeeAllSpecClicked(comparisonListModel: ComparisonListModel)
}
