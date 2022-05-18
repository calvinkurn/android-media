package com.tokopedia.recommendation_widget_common.presentation.model

/**
 * Created by yfsx on 30/11/21.
 */
data class RecomFilterResult (
    val pageName: String = "",
    val recomWidgetData: RecommendationWidget? = null,
    val filterList: List<AnnotationChip> = listOf(),
    val isSuccess: Boolean = false,
    val throwable: Throwable? = null
)