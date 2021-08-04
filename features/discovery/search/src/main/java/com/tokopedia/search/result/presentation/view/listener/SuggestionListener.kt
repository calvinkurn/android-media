package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.SuggestionDataView

interface SuggestionListener {
    fun onSuggestionClicked(suggestionDataView: SuggestionDataView?)
}