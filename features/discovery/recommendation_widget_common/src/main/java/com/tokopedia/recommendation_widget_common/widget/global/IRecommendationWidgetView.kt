package com.tokopedia.recommendation_widget_common.widget.global

/**
 * Created by Frenzel
 */
interface IRecommendationWidgetView<T : RecommendationVisitable> {
    fun bind(model: T)
}
