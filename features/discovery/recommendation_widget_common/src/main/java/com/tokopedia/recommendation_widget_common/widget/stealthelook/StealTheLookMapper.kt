package com.tokopedia.recommendation_widget_common.widget.stealthelook

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationVisitable
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.recommendation_widget_common.widget.stealthelook.tracking.StealTheLookTracking

object StealTheLookMapper {

    fun RecommendationWidgetModel.asStealTheLookModel(
        data: RecommendationWidget,
        userId: String
    ): StealTheLookWidgetModel {
        val tracking = getTracking(data, source, userId)
        return StealTheLookWidgetModel(
            visitable = RecommendationVisitable.create(metadata, trackingModel, userId, data.appLog),
            widget = data,
            itemList = mapStealTheLookItem(data, tracking),
        )
    }

    private fun mapStealTheLookItem(
        recommendationWidget: RecommendationWidget,
        tracking: StealTheLookTracking?,
    ): List<StealTheLookStyleModel> {
        return recommendationWidget.recommendationItemList.chunked(StealTheLookStyleModel.GRID_COUNT)
            .filter { it.size == StealTheLookStyleModel.GRID_COUNT }
            .mapIndexed { index, recommendationItems ->
                mapToStyleModel(
                    stylePosition = index,
                    recommendationItems = recommendationItems,
                    tracking = tracking,
                )
            }
    }

    private fun mapToStyleModel(
        stylePosition: Int,
        recommendationItems: List<RecommendationItem>,
        tracking: StealTheLookTracking?
    ): StealTheLookStyleModel {
        return StealTheLookStyleModel(
            stylePosition = stylePosition,
            grids = recommendationItems.map {
                StealTheLookGridModel(
                    stylePosition,
                    it,
                )
            },
            tracking = tracking
        )
    }

    private fun getTracking(
        widget: RecommendationWidget,
        source: RecommendationWidgetSource?,
        userId: String,
    ): StealTheLookTracking? {
        return StealTheLookTracking.Factory.create(widget, source, userId)
    }
}
