package com.tokopedia.recommendation_widget_common.widget.stealthelook

import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationVisitable
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.recommendation_widget_common.widget.stealthelook.tracking.StealTheLookTracking

object StealTheLookMapper {

    fun RecommendationWidgetModel.asStealTheLookModel(
        data: RecommendationWidget,
        appLogAdditionalParam: AppLogAdditionalParam,
        userId: String
    ): StealTheLookWidgetModel {
        val tracking = getTracking(data, source, userId)
        return StealTheLookWidgetModel(
            visitable = RecommendationVisitable.create(metadata, trackingModel, userId, data.appLog, appLogAdditionalParam),
            widget = data,
            itemList = mapStealTheLookItem(data, tracking, appLogAdditionalParam),
        )
    }

    private fun mapStealTheLookItem(
        recommendationWidget: RecommendationWidget,
        tracking: StealTheLookTracking?,
        appLogAdditionalParam: AppLogAdditionalParam,
    ): List<StealTheLookStyleModel> {
        return recommendationWidget.recommendationItemList.chunked(StealTheLookStyleModel.GRID_COUNT)
            .filter { it.size == StealTheLookStyleModel.GRID_COUNT }
            .mapIndexed { index, recommendationItems ->
                mapToStyleModel(
                    stylePosition = index,
                    recommendationItems = recommendationItems,
                    tracking = tracking,
                    appLogAdditionalParam = appLogAdditionalParam,
                )
            }
    }

    private fun mapToStyleModel(
        stylePosition: Int,
        recommendationItems: List<RecommendationItem>,
        tracking: StealTheLookTracking?,
        appLogAdditionalParam: AppLogAdditionalParam,
    ): StealTheLookStyleModel {
        return StealTheLookStyleModel(
            stylePosition = stylePosition,
            grids = recommendationItems.map {
                StealTheLookGridModel(
                    stylePosition,
                    it,
                )
            },
            tracking = tracking,
            appLogAdditionalParam = appLogAdditionalParam,
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
