package com.tokopedia.recommendation_widget_common.widget.global

/**
 * Created by Frenzel
 */
interface IRecommendationWidgetView<T : RecommendationVisitable> {

    val layoutId: Int
    fun bind(model: T)
    fun recycle()
}
