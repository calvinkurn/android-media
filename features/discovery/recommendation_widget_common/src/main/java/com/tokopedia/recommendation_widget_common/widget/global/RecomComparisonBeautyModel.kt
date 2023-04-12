package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

/**
 * Created by frenzel on 27/03/23
 */
data class RecomComparisonBeautyModel(
    override val type: String = "",
    override val name: String = "",
    override var recomWidgetData: RecommendationWidget? = null,
    override val state: Int = 0,
    override val verticalPosition: Int = -1,
    override var isInitialized: Boolean = false,
) : RecomVisitable {
    override fun type(typeFactory: RecomTypeFactory): Int {
        return typeFactory.type(this)
    }
}
