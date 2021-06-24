package com.tokopedia.tokomart.search.presentation.listener

import com.tokopedia.tokomart.search.presentation.model.SuggestionDataView

interface SuggestionListener {

    fun onSuggestionClicked(suggestionDataView: SuggestionDataView)
}