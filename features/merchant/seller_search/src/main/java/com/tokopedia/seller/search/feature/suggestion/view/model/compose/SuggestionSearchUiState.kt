package com.tokopedia.seller.search.feature.suggestion.view.model.compose

import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller

data class SuggestionSearchUiState(
    val suggestionSellerSearchList: List<BaseSuggestionSearchSeller> = emptyList(),
    val isInsertSuccessSearch: Boolean = false,
    val throwable: Throwable? = null
)
