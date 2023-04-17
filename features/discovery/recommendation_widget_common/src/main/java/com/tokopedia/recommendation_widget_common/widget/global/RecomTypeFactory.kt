package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.RecomComparisonBpcModel

/**
 * Created by frenzel on 11/03/23
 */
interface RecomTypeFactory {
    fun type(model: RecomComparisonBpcModel): Int
    fun type(model: RecomCarouselModel): Int
    fun createView(context: Context, type: Int): BaseRecomWidgetView<*>
}
