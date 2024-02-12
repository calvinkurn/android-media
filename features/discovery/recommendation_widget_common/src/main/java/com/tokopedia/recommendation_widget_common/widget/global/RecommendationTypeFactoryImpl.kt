package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import android.view.ViewGroup
import com.tokopedia.recommendation_widget_common.RecomTemporary
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselModel
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselQeOldWidgetView
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselWidgetView
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.ComparisonBpcWidgetView
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.RecommendationComparisonBpcModel
import com.tokopedia.recommendation_widget_common.widget.loading.RecommendationCarouselShimmeringModel
import com.tokopedia.recommendation_widget_common.widget.loading.RecommendationCarouselShimmeringView
import com.tokopedia.recommendation_widget_common.widget.loading.StealTheLookShimmeringModel
import com.tokopedia.recommendation_widget_common.widget.loading.StealTheLookShimmeringView
import com.tokopedia.recommendation_widget_common.widget.stealthelook.StealTheLookWidgetModel
import com.tokopedia.recommendation_widget_common.widget.stealthelook.StealTheLookWidgetView
import com.tokopedia.recommendation_widget_common.widget.vertical.RecommendationVerticalView
import com.tokopedia.recommendation_widget_common.widget.vertical.RecommendationVerticalModel

/**
 * Created by frenzel on 11/03/23
 */
class RecommendationTypeFactoryImpl : RecommendationTypeFactory {

    override fun type(model: RecommendationComparisonBpcModel): Int {
        return ComparisonBpcWidgetView.LAYOUT
    }

    @RecomTemporary
    override fun type(model: RecommendationCarouselModel): Int {
        return if (model.widget.hasQuantityEditor()) {
            RecommendationCarouselQeOldWidgetView.LAYOUT
        } else {
            RecommendationCarouselWidgetView.LAYOUT
        }
    }

    override fun type(model: RecommendationCarouselShimmeringModel): Int {
        return RecommendationCarouselShimmeringView.LAYOUT
    }

    override fun type(model: RecommendationVerticalModel): Int {
        return RecommendationVerticalView.LAYOUT
    }

    override fun type(model: StealTheLookWidgetModel): Int {
        return StealTheLookWidgetView.LAYOUT
    }

    override fun type(model: StealTheLookShimmeringModel): Int {
        return StealTheLookShimmeringView.LAYOUT
    }

    override fun createView(context: Context, model: RecommendationVisitable): ViewGroup {
        return when (model.type(this)) {
            ComparisonBpcWidgetView.LAYOUT -> ComparisonBpcWidgetView(context)
            RecommendationCarouselWidgetView.LAYOUT -> RecommendationCarouselWidgetView(context)
            RecommendationCarouselQeOldWidgetView.LAYOUT -> RecommendationCarouselQeOldWidgetView(context)
            RecommendationCarouselShimmeringView.LAYOUT -> RecommendationCarouselShimmeringView(context)
            RecommendationVerticalView.LAYOUT -> RecommendationVerticalView(context)
            StealTheLookWidgetView.LAYOUT -> StealTheLookWidgetView(context)
            StealTheLookShimmeringView.LAYOUT -> StealTheLookShimmeringView(context)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
}
