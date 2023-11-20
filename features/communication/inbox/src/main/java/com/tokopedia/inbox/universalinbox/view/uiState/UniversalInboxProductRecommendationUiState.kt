package com.tokopedia.inbox.universalinbox.view.uiState

import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationUiModel

data class UniversalInboxProductRecommendationUiState(
    val title: String = "",
    val productRecommendation: List<UniversalInboxRecommendationUiModel> = listOf(),
    val isLoading: Boolean = false
)
