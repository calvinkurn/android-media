package com.tokopedia.autocomplete.suggestion

interface SuggestionListener {
    fun copyTextToSearchView(text: String)

    fun onItemClicked(item: BaseSuggestionDataView)

    fun onItemImpressed(item: BaseSuggestionDataView)
}