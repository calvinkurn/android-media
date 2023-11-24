package com.tokopedia.autocompletecomponent.searchbar

import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import org.junit.Test

internal class SearchBarAddKeywordMPSTest : SearchBarViewModelTestFixtures() {

    @Test
    fun `add keyword by suggestion option`() {
        val query = "samsung"
        `Given mps enabled and no coach mark should be displayed`()
        val suggestionData = BaseSuggestionDataView(
            title = query
        )
        val keyword = SearchBarKeyword(
            position = 0,
            keyword = query,
        )
        val keywords = listOf(keyword)

        `When keyword is added`(suggestionData)

        `Then verify SearchBarKeyword list`(keywords)
        `Then verify searchBarKeywordError`(null)
    }

    @Test
    fun `add keyword by suggestion option with empty title`() {
        val query = ""
        `Given mps enabled and no coach mark should be displayed`()
        val suggestionData = BaseSuggestionDataView(
            title = query
        )
        `When keyword is added`(suggestionData)

        `Then verify searchBarKeywordError`(SearchBarKeywordError.Empty)
    }

    private fun `When keyword is added`(
        suggestion: BaseSuggestionDataView
    ) {
        viewModel.onKeywordAdded(suggestion)
    }
}
