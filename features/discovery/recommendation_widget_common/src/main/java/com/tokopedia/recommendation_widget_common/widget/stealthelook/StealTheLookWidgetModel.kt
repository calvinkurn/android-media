package com.tokopedia.recommendation_widget_common.widget.stealthelook

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationVisitable
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.recommendation_widget_common.widget.vertical.tracking.RecommendationVerticalTracking

data class StealTheLookWidgetModel(
    val visitable: RecommendationVisitable,
    val widget: RecommendationWidget,
    val itemList: List<StealTheLookPageModel>,
    val source: RecommendationWidgetSource?,
    val listener: RecommendationWidgetListener? = null,
) : RecommendationVisitable by visitable {

    val widgetTracking: RecommendationVerticalTracking? =
        RecommendationVerticalTracking.Factory.create(widget, source, userId)

    override fun type(typeFactory: RecommendationTypeFactory?): Int =
        typeFactory?.type(this) ?: 0

    companion object {
        private const val GRID_COUNT = 3

        fun from(
            model: RecommendationWidgetModel,
            data: RecommendationWidget,
            userId: String
        ): StealTheLookWidgetModel = StealTheLookWidgetModel(
            visitable = RecommendationVisitable.create(model.metadata, model.trackingModel, userId),
            widget = data,
            itemList = mapStealTheLookItem(data),
            source = model.source,
            listener = model.listener,
        )

        private fun mapStealTheLookItem(recommendationWidget: RecommendationWidget): List<StealTheLookPageModel> {
            return recommendationWidget.recommendationItemList.chunked(GRID_COUNT)
                .mapIndexed { index, recommendationItems ->
                    StealTheLookPageModel(
                        page = index,
                        recomItemList = recommendationItems
                    )
            }
        }
    }
}
