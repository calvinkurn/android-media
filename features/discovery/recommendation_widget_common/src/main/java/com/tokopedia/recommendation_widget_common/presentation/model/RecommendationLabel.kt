package com.tokopedia.recommendation_widget_common.presentation.model

/**
 * Created by Lukas on 2/15/21.
 */
data class RecommendationLabel(
    val title: String = "",
    val type: String = "",
    val position: String = "",
    val imageUrl: String = "",
    val styles: List<Style> = emptyList(),
) {

    data class Style(
        val key: String = "",
        val value: String = "",
    )
}
