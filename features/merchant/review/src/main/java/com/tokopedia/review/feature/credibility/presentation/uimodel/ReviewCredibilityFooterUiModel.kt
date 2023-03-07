package com.tokopedia.review.feature.credibility.presentation.uimodel

import com.tokopedia.reviewcommon.uimodel.StringRes

data class ReviewCredibilityFooterUiModel(val description: StringRes, val button: Button) {
    data class Button(val text: String, val appLink: String)
}