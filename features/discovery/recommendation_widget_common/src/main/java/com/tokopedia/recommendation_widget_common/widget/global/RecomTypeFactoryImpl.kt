package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.ComparisonBpcWidgetView
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.RecomComparisonBpcModel

/**
 * Created by frenzel on 11/03/23
 */
class RecomTypeFactoryImpl : RecomTypeFactory {
    override fun type(model: RecomComparisonBpcModel): Int {
        return ComparisonBpcWidgetView.LAYOUT
    }

    override fun type(model: RecomCarouselModel): Int {
        return RecomCarouselWidgetView.LAYOUT
    }

    override fun createView(context: Context, model: RecomVisitable): BaseRecomWidgetView<out RecomVisitable> {
        return when (model.type(this)) {
            ComparisonBpcWidgetView.LAYOUT -> ComparisonBpcWidgetView(context)
            RecomCarouselWidgetView.LAYOUT -> RecomCarouselWidgetView(context)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
}
