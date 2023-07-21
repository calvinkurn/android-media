package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import android.view.ViewGroup
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselModel
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.RecommendationComparisonBpcModel
import com.tokopedia.recommendation_widget_common.widget.loading.RecommendationCarouselShimmeringModel
import com.tokopedia.recommendation_widget_common.widget.vertical.RecommendationVerticalModel

/**
 * Created by frenzel on 11/03/23
 */
interface RecommendationTypeFactory {
    fun type(model: RecommendationComparisonBpcModel): Int
    fun type(model: RecommendationCarouselModel): Int
    fun type(model: RecommendationCarouselShimmeringModel): Int
    fun type(model: RecommendationVerticalModel): Int
    fun createView(context: Context, model: RecommendationVisitable): ViewGroup
}
