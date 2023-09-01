package com.tokopedia.seller.search.feature.suggestion.view.model.compose

import androidx.compose.runtime.Stable
import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller

@Stable
data class SuggestionSearchUiState(
    val suggestionSellerSearchList: List<BaseSuggestionSearchSeller> = emptyList(),
    val isLoadingState: Boolean = false,
    val isDismissedKeyboard: Boolean = false,
    val throwable: Throwable? = null
)
