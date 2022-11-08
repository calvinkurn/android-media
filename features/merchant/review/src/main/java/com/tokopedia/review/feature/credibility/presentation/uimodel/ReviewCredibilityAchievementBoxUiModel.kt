package com.tokopedia.review.feature.credibility.presentation.uimodel

data class ReviewCredibilityAchievementBoxUiModel(
    val title: String,
    val label: String,
    val achievements: List<ReviewCredibilityAchievementUiModel>,
    val cta: Button
) {
    data class ReviewCredibilityAchievementUiModel(
        val avatar: String,
        val name: String,
        val color: String,
        val mementoLink: String
    )

    data class Button(
        val text: String, val appLink: String
    )
}
