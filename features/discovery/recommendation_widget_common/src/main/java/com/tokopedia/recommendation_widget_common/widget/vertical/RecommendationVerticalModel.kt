package com.tokopedia.recommendation_widget_common.widget.vertical

import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationVisitable
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel
import com.tokopedia.recommendation_widget_common.widget.vertical.tracking.RecommendationVerticalTracking

data class RecommendationVerticalModel(
    val visitable: RecommendationVisitable,
    val widget: RecommendationWidget,
    val source: RecommendationWidgetSource?,
    val listener: RecommendationWidgetListener? = null,
) : RecommendationVisitable by visitable {

    val widgetTracking: RecommendationVerticalTracking? =
        RecommendationVerticalTracking.Factory.create(widget, source, userId)
    override fun type(typeFactory: RecommendationTypeFactory?): Int =
        typeFactory?.type(this) ?: 0

    companion object {

        fun from(
            metadata: RecommendationWidgetMetadata,
            trackingModel: RecommendationWidgetTrackingModel,
            recommendationWidget: RecommendationWidget,
            source: RecommendationWidgetSource?,
            appLogAdditionalParam: AppLogAdditionalParam,
            listener: RecommendationWidgetListener?,
            userId: String
        ): RecommendationVerticalModel = RecommendationVerticalModel(
            visitable = RecommendationVisitable.create(
                metadata,
                trackingModel,
                userId,
                recommendationWidget.appLog,
                appLogAdditionalParam
            ),
            widget = recommendationWidget,
            source = source,
            listener = listener,
        )
    }
}
