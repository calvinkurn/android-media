package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.SuggestionDataView

interface SuggestionListener {
    fun onSuggestionImpressed(suggestionDataView: SuggestionDataView)
    fun onSuggestionClicked(suggestionDataView: SuggestionDataView)
}