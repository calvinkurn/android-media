package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselModel
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.RecommendationComparisonBpcModel

/**
 * Created by frenzel on 11/03/23
 */
interface RecommendationTypeFactory {
    fun type(model: RecommendationComparisonBpcModel): Int
    fun type(model: RecommendationCarouselModel): Int
    fun createView(context: Context, model: RecommendationVisitable): BaseRecommendationWidgetView<out RecommendationVisitable>
}
