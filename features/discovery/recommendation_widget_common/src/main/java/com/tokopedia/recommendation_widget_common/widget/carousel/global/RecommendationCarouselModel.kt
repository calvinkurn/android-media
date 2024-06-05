package com.tokopedia.recommendation_widget_common.widget.carousel.global

import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.global.tracking.RecommendationCarouselWidgetTracking
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationVisitable
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel

/**
 * Created by frenzel on 27/03/23
 */
data class RecommendationCarouselModel(
    val visitable: RecommendationVisitable,
    val widget: RecommendationWidget,
    val source: RecommendationWidgetSource?,
    val listener: RecommendationWidgetListener?,
) : RecommendationVisitable by visitable {

    val widgetTracking: RecommendationCarouselWidgetTracking? =
        RecommendationCarouselWidgetTracking.Factory.create(widget, source)

    override fun type(typeFactory: RecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    val isTokonow: Boolean = this.metadata.pageName == PAGE_NAME_TOKONOW
    val hasData: Boolean
        get() = widget.recommendationItemList.isNotEmpty()

    fun getItem(index: Int): RecommendationItem? {
        return widget.recommendationItemList.getOrNull(index)
    }

    companion object {
        private const val PAGE_NAME_TOKONOW = "pdp_9_tokonow"

        fun from(
            metadata: RecommendationWidgetMetadata,
            trackingModel: RecommendationWidgetTrackingModel,
            widget: RecommendationWidget,
            source: RecommendationWidgetSource?,
            appLogAdditionalParam: AppLogAdditionalParam,
            listener: RecommendationWidgetListener?,
            userId: String,
        ) = RecommendationCarouselModel(
            visitable = RecommendationVisitable.create(
                metadata,
                trackingModel,
                userId,
                widget.appLog,
                appLogAdditionalParam
            ),
            widget = widget,
            source = source,
            listener = listener,
        )
    }
}
