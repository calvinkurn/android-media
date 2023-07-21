package com.tokopedia.recommendation_widget_common.widget.vertical

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationVisitable
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel

data class RecommendationVerticalModel(
    val visitable: RecommendationVisitable,
    val widget: RecommendationWidget
) : RecommendationVisitable by visitable {

    override fun type(typeFactory: RecommendationTypeFactory?): Int =
        typeFactory?.type(this) ?: 0

    companion object {

        fun from(
            metadata: RecommendationWidgetMetadata,
            trackingModel: RecommendationWidgetTrackingModel,
            recommendationWidget: RecommendationWidget,
        ): RecommendationVerticalModel = RecommendationVerticalModel(
            visitable = RecommendationVisitable.create(metadata, trackingModel),
            widget = recommendationWidget
        )
    }
}
