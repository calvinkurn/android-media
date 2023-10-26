package com.tokopedia.recommendation_widget_common.widget.recommendationcard.model

data class RecommendationEntryPointCardUiModel(
    val productTitle: String,
    val productSubTitle: String,
    val productImageUrl: String,
    val backgroundCard: String
) {
    data class LabelState(
        val imageUrl: String,
        val title: String,
        val textColor: String
    )
}
