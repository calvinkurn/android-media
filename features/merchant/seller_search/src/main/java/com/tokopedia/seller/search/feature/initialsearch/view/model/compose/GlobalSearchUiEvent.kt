package com.tokopedia.seller.search.feature.initialsearch.view.model.compose

import androidx.compose.ui.text.TextRange

sealed class GlobalSearchUiEvent {
    object OnSearchBarCleared : GlobalSearchUiEvent()
    data class OnBackButtonClicked(val searchBarKeyword: String) : GlobalSearchUiEvent()
    data class OnKeyboardSearchSubmit(val searchBarKeyword: String) : GlobalSearchUiEvent()
    data class OnKeywordTextChanged(val searchBarKeyword: String, val selection: TextRange) : GlobalSearchUiEvent()

    data class OnSearchResultKeyword(val searchBarKeyword: String) : GlobalSearchUiEvent()
}
