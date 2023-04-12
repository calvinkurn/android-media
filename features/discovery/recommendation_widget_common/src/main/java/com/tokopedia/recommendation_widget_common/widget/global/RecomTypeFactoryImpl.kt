package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context

/**
 * Created by frenzel on 11/03/23
 */
class RecomTypeFactoryImpl: RecomTypeFactory {
    override fun type(model: RecomComparisonBeautyModel): Int {
        return ComparisonBeautyWidgetView.LAYOUT
    }

    override fun type(model: RecomCarouselModel): Int {
        return RecomCarouselWidgetView.LAYOUT
    }

    override fun createView(context: Context, type: Int): BaseRecomWidget {
        return when(type) {
            ComparisonBeautyWidgetView.LAYOUT -> ComparisonBeautyWidgetView(context)
            RecomCarouselWidgetView.LAYOUT -> RecomCarouselWidgetView(context)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
}
