package com.tokopedia.autocompletecomponent.suggestion

interface SuggestionListener {
    fun copyTextToSearchView(text: String)

    fun onItemClicked(item: BaseSuggestionDataView)

    fun onChipClicked(item: BaseSuggestionDataView.ChildItem)

    fun onItemImpressed(item: BaseSuggestionDataView)
}