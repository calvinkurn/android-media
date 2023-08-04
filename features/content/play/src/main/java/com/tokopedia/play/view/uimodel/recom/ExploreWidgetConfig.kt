package com.tokopedia.play.view.uimodel.recom

/**
 * @author by astidhiyaa on 29/11/22
 */

sealed class PlayChannelRecommendationConfig
data class ExploreWidgetConfig(
    val group: String = "",
    val sourceType: String = "",
    val sourceId: String = "",
    val categoryName: String = "",
) : PlayChannelRecommendationConfig()

data class CategoryWidgetConfig(
    val categoryGroup: String = "",
    val hasCategory: Boolean = false,
    val categoryName: String = "",
    val categorySourceType: String = "",
    val categorySourceId: String = "",
    val categoryLevel: Int = 0,
    val categoryId: String = "",
) : PlayChannelRecommendationConfig()
