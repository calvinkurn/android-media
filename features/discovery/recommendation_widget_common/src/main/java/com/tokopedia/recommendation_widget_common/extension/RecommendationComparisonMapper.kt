package com.tokopedia.recommendation_widget_common.extension

import android.content.Context
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetMapper

object RecommendationComparisonMapper {
    fun mapRecommendationToComparisonWidgetModel(
            recommendationWidget: RecommendationWidget,
            context: Context,
    ): ComparisonListModel {
        return ComparisonWidgetMapper.mapToComparisonWidgetModel(
                recommendationWidget, context
        )
    }
}