package com.tokopedia.recommendation_widget_common.viewutil

import android.content.Context
import android.view.View
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetView

/**
 * Created by yfsx on 5/3/21.
 */
private val LAYOUT_TYPE_CATEGORY_WIDGET = "Category-Widget"
private val LAYOUT_TYPE_CAROUSEL = "carousel"
private val LAYOUT_TYPE_INFINITE = "infinite"
private val LAYOUT_TYPE_CUSTOM_HORIZONTAL = "custom-horizontal"
private val LAYOUT_TYPE_CUSTOM_VERTICAL = "custom-vertical"

fun getAndBindRecommendationWidgetByLayoutType(
        context: Context,
        data: RecommendationWidget,
        adapterPosition: Int = 0,
        recommendationCarouselWidgetListener: RecommendationCarouselWidgetListener): View? {
    var layoutView: View? = null
    when (data.layoutType) {
        LAYOUT_TYPE_CATEGORY_WIDGET ->  {
            layoutView = RecommendationCarouselWidgetView(context)
            (layoutView as RecommendationCarouselWidgetView).bind(RecommendationCarouselData(data), adapterPosition, recommendationCarouselWidgetListener)
        }
        LAYOUT_TYPE_CAROUSEL -> {

        }
        LAYOUT_TYPE_INFINITE -> {

        }
        LAYOUT_TYPE_CUSTOM_HORIZONTAL -> {

        }
        LAYOUT_TYPE_CUSTOM_VERTICAL -> {

        }
    }

    return layoutView
}