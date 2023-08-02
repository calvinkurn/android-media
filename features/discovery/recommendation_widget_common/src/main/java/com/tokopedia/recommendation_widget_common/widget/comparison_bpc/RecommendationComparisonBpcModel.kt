package com.tokopedia.recommendation_widget_common.widget.comparison_bpc

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationVisitable
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel

/**
 * Created by frenzel on 27/03/23
 */
data class RecommendationComparisonBpcModel(
    val recommendationVisitable: RecommendationVisitable,
    val recommendationWidget: RecommendationWidget,
) : RecommendationVisitable by recommendationVisitable {
    override fun type(typeFactory: RecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {

        fun from(
            metadata: RecommendationWidgetMetadata,
            trackingModel: RecommendationWidgetTrackingModel,
            recommendationWidget: RecommendationWidget,
        ): RecommendationComparisonBpcModel =
            RecommendationComparisonBpcModel(
                RecommendationVisitable.create(metadata, trackingModel),
                recommendationWidget,
            )
    }
}
