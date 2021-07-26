package com.tokopedia.tokopedianow.search.presentation.listener

import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView

interface SuggestionListener {

    fun onSuggestionClicked(suggestionDataView: SuggestionDataView)
}