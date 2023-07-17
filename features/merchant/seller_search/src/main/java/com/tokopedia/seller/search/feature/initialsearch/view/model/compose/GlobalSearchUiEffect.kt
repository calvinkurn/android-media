package com.tokopedia.seller.search.feature.initialsearch.view.model.compose

sealed class GlobalSearchUiEffect {
    object OnSearchBarCleared : GlobalSearchUiEffect()
    data class OnBackButtonClicked(val searchBarKeyword: String) : GlobalSearchUiEffect()
    data class OnKeyboardSearchSubmit(val searchBarKeyword: String) : GlobalSearchUiEffect()
    data class OnKeywordTextChanged(val searchBarKeyword: String) : GlobalSearchUiEffect()
}
