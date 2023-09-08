package com.tokopedia.inbox.universalinbox.view.uiState

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class UniversalInboxProductRecommendationUiState(
    val title: String = "",
    val productList: List<RecommendationItem> = listOf(),
    val isLoading: Boolean = false
)
