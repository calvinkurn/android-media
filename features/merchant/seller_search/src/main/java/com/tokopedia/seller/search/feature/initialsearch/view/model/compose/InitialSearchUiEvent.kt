package com.tokopedia.seller.search.feature.initialsearch.view.model.compose

sealed class InitialSearchUiEvent {

    object OnClearAllKeyword : InitialSearchUiEvent()
    data class OnItemClicked(val keyword: String, val searchBarKeyword: String) : InitialSearchUiEvent()
    data class OnItemRemoveClicked(val keyword: String, val position: Int) : InitialSearchUiEvent()
}
