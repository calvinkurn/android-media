package com.tokopedia.recommendation_widget_common.widget.recommendationcard.model

data class RecomEntryPointCardUiModel(
    val productTitle: String,
    val productSubTitle: String,
    val productImageUrl: String,
    val backgroundColor: String
) {
    data class LabelState(
        val iconUrl: String,
        val title: String,
        val textColor: String
    )
}
