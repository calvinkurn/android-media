package com.tokopedia.recommendation_widget_common.widget.global

interface IRecommendationWidgetView<T : RecommendationVisitable> {
    fun bind(model: T)
}
