package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import android.view.ViewGroup
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselModel
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselWidgetView
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.ComparisonBpcWidgetView
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.RecommendationComparisonBpcModel

/**
 * Created by frenzel on 11/03/23
 */
class RecommendationTypeFactoryImpl : RecommendationTypeFactory {
    override fun type(model: RecommendationComparisonBpcModel): Int {
        return ComparisonBpcWidgetView.LAYOUT
    }

    override fun type(model: RecommendationCarouselModel): Int {
        return RecommendationCarouselWidgetView.LAYOUT
    }

    override fun createView(context: Context, model: RecommendationVisitable): ViewGroup {
        return when (model.type(this)) {
            ComparisonBpcWidgetView.LAYOUT -> ComparisonBpcWidgetView(context)
            RecommendationCarouselWidgetView.LAYOUT -> RecommendationCarouselWidgetView(context)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
}
