package com.tokopedia.autocomplete.suggestion

interface SuggestionViewUpdateListener {

    fun showSuggestionView()

    fun setSearchQuery(keyword: String)

    fun dropKeyboard()
}