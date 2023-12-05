package com.tokopedia.recommendation_widget_common.widget.stealthelook

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationVisitable

data class StealTheLookWidgetModel(
    val visitable: RecommendationVisitable,
    val widget: RecommendationWidget,
    val itemList: List<StealTheLookStyleModel>,
) : RecommendationVisitable by visitable {

    override fun type(typeFactory: RecommendationTypeFactory?): Int =
        typeFactory?.type(this) ?: 0

}
