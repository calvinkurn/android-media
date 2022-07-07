package com.tokopedia.review.feature.credibility.presentation.uimodel

data class ReviewCredibilityFooterUiModel(val description: String, val button: Button) {
    data class Button(val text: String, val appLink: String)
}