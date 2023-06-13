package com.tokopedia.search.result.product.suggestion

interface SuggestionListener {
    fun onSuggestionImpressed(suggestionDataView: SuggestionDataView)
    fun onSuggestionClicked(suggestionDataView: SuggestionDataView)
}
