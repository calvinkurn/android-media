package com.tokopedia.search.result.product.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.di.scope.SearchScope
import javax.inject.Inject

@SearchScope
class SuggestionPresenter @Inject constructor() {

    private var suggestionDataView: SuggestionDataView? = null

    fun setSuggestionDataView(suggestionDataView: SuggestionDataView?) {
        this.suggestionDataView = suggestionDataView
    }

    fun appendSuggestion(visitableList: MutableList<Visitable<*>>) {
        suggestionDataView?.let {
            if (it.suggestionText.isNotEmpty())
                visitableList.add(it)

            suggestionDataView = null
        }
    }

    fun processSuggestion(responseCode: String, action: (Visitable<*>) -> Unit) {
        if (!shouldShowSuggestion(responseCode)) return

        suggestionDataView?.let { action(it) }
    }

    private fun shouldShowSuggestion(responseCode: String): Boolean {
        return showSuggestionResponseCodeList.contains(responseCode)
            && (suggestionDataView?.suggestionText?.isNotEmpty() == true)
    }

    companion object {
        private val showSuggestionResponseCodeList = listOf("3", "6", "7")
    }
}
