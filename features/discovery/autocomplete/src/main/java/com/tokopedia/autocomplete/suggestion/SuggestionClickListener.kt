package com.tokopedia.autocomplete.suggestion

interface SuggestionClickListener {
    fun copyTextToSearchView(text: String)

    fun onItemClicked(item: BaseSuggestionDataView)

    fun onChipClicked(item: BaseSuggestionDataView.ChildItem)
}