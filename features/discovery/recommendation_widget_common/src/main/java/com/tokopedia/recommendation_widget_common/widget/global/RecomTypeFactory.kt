package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context

/**
 * Created by frenzel on 11/03/23
 */
interface RecomTypeFactory {
    fun type(model: RecomComparisonBeautyModel): Int
    fun type(model: RecomCarouselModel): Int
    fun createView(context: Context, type: Int): BaseRecomWidget
}
