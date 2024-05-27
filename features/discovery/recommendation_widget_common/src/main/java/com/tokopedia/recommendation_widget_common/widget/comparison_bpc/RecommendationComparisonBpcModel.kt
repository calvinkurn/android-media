package com.tokopedia.recommendation_widget_common.widget.comparison_bpc

import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationVisitable
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel

/**
 * Created by frenzel on 27/03/23
 */
data class RecommendationComparisonBpcModel(
    val recommendationVisitable: RecommendationVisitable,
    val recommendationWidget: RecommendationWidget,
    val listener: RecommendationWidgetListener? = null,
) : RecommendationVisitable by recommendationVisitable {
    override fun type(typeFactory: RecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {

        fun from(
            metadata: RecommendationWidgetMetadata,
            trackingModel: RecommendationWidgetTrackingModel,
            recommendationWidget: RecommendationWidget,
            appLogAdditionalParam: AppLogAdditionalParam,
            listener: RecommendationWidgetListener?,
            userId: String
        ): RecommendationComparisonBpcModel =
            RecommendationComparisonBpcModel(
                RecommendationVisitable.create(metadata, trackingModel, userId, recommendationWidget.appLog, appLogAdditionalParam),
                recommendationWidget,
                listener
            )
    }
}
