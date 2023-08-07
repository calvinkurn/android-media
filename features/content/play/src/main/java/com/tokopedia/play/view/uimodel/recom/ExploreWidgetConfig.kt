package com.tokopedia.play.view.uimodel.recom

/**
 * @author by astidhiyaa on 29/11/22
 */

data class ExploreWidgetConfig(
    val group: String = "",
    val sourceType: String = "",
    val sourceId: String = "",
    val categoryName: String = "",
)

data class CategoryWidgetConfig(
    val categoryGroup: String = "",
    val hasCategory: Boolean = false,
    val categoryName: String = "",
    val categorySourceType: String = "",
    val categorySourceId: String = "",
    val categoryLevel: Int = 0,
    val categoryId: String = "",
)
data class PlayChannelRecommendationConfig(
    val exploreWidgetConfig: ExploreWidgetConfig = ExploreWidgetConfig(),
    val categoryWidgetConfig: CategoryWidgetConfig = CategoryWidgetConfig(),
)
