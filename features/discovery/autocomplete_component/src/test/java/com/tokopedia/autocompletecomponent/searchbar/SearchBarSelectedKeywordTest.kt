package com.tokopedia.autocompletecomponent.searchbar

import com.tokopedia.autocompletecomponent.shouldBe
import org.junit.Test

internal class SearchBarSelectedKeywordTest : SearchBarViewModelTestFixtures() {
    @Test
    fun `select keyword success`() {
        `Given mps enabled and no coach mark should be displayed`()
        val query = "samsung"
        val keyword = SearchBarKeyword(
            position = 0,
            keyword = query,
        )
        `Given search bar keyword list already populated`(listOf(keyword))

        viewModel.onKeywordSelected(keyword)

        val expectedKeyword = keyword.copy(
            isSelected = true
        )
        `Then verify active keyword`(expectedKeyword)
        `Then verify latest keyword`(expectedKeyword)
        `Then verify SearchBarKeyword list`(listOf(expectedKeyword))
        `Then verify mps state`(
            SearchBarState(
                isMpsEnabled = true,
                isAddButtonEnabled = false,
                isKeyboardDismissEnabled = false,
                shouldDisplayMpsPlaceHolder = true,
            )
        )
    }

    @Test
    fun `unselect keyword success`() {
        `Given mps enabled and no coach mark should be displayed`()
        val query = "samsung"
        val keyword = SearchBarKeyword(
            position = 0,
            keyword = query,
            isSelected = true,
        )
        `Given search bar keyword list already populated`(listOf(keyword))

        viewModel.onKeywordSelected(keyword)

        val expectedActiveKeyword = SearchBarKeyword(position = 1)
        `Then verify active keyword`(expectedActiveKeyword)
        `Then verify latest keyword`(expectedActiveKeyword)
        val expectedKeyword = keyword.copy(isSelected = false)
        `Then verify SearchBarKeyword list`(listOf(expectedKeyword))
        `Then verify mps state`(
            SearchBarState(
                isMpsEnabled = true,
                isAddButtonEnabled = true,
                isKeyboardDismissEnabled = false,
                shouldDisplayMpsPlaceHolder = true,
                isMpsAnimationEnabled = true,
            )
        )
    }

    @Test
    fun `unselect keyword with edited keyword already in list should fail`() {
        `Given mps enabled and no coach mark should be displayed`()
        val query = "samsung"
        val keyword1 = SearchBarKeyword(
            position = 0,
            keyword = "samsung galaxy",
        )
        val keyword2 = SearchBarKeyword(
            position = 1,
            keyword = query,
        )
        val keywords = listOf(
            keyword1,
            keyword2,
        )
        `Given search bar keyword list already populated`(keywords)

        viewModel.onKeywordSelected(keyword1)
        viewModel.onQueryUpdated(query)
        val activeKeyword = keyword1.copy(keyword = query, isSelected = true)
        viewModel.onKeywordSelected(activeKeyword)

        val expectedKeywords = listOf(activeKeyword, keyword2)

        `Then verify active keyword`(activeKeyword)
        `Then verify latest keyword`(keyword1.copy(isSelected = true))
        `Then verify SearchBarKeyword list`(expectedKeywords)
        `Then verify searchBarKeywordError`(SearchBarKeywordError.Duplicate)
        `Then verify mps state`(
            SearchBarState(
                isMpsEnabled = true,
                isAddButtonEnabled = false,
                isKeyboardDismissEnabled = false,
                shouldDisplayMpsPlaceHolder = true,
            )
        )
    }

    private fun `Then verify active keyword`(searchBarKeyword: SearchBarKeyword) {
        viewModel.activeKeyword shouldBe searchBarKeyword
    }

    private fun `Then verify latest keyword`(searchBarKeyword: SearchBarKeyword) {
        viewModel.activeKeywordLiveData.value shouldBe searchBarKeyword
    }

    @Test
    fun `apply suggestion with different keyword should replace selected keyword and end selection`() {
        `Given mps enabled and no coach mark should be displayed`()
        val query = "samsung"
        val keyword1 = SearchBarKeyword(
            position = 0,
            keyword = "samsung galaxy",
        )
        val keyword2 = SearchBarKeyword(
            position = 1,
            keyword = query,
        )
        val keywords = listOf(
            keyword1,
            keyword2,
        )
        `Given search bar keyword list already populated`(keywords)

        viewModel.onKeywordSelected(keyword1)
        val selectedKeyword = keyword1.copy(isSelected = true)
        val suggestionText = "samsung s21"
        viewModel.onApplySuggestionToSelectedKeyword(suggestionText, selectedKeyword)

        val updatedKeyword = keyword1.copy(keyword = suggestionText)
        val expectedKeywords = listOf(updatedKeyword, keyword2)
        val activeKeyword = SearchBarKeyword(position = expectedKeywords.size)

        `Then verify active keyword`(activeKeyword)
        `Then verify latest keyword`(activeKeyword)
        `Then verify SearchBarKeyword list`(expectedKeywords)
        `Then verify mps state`(
            SearchBarState(
                isMpsEnabled = true,
                isMpsAnimationEnabled = true,
                isAddButtonEnabled = true,
                isKeyboardDismissEnabled = false,
                shouldDisplayMpsPlaceHolder = true,
            )
        )
    }

    @Test
    fun `apply suggestion with keyword that already in keyword list should not replace selected keyword`() {
        `Given mps enabled and no coach mark should be displayed`()
        val query = "samsung"
        val keyword1 = SearchBarKeyword(
            position = 0,
            keyword = "samsung galaxy",
        )
        val keyword2 = SearchBarKeyword(
            position = 1,
            keyword = query,
        )
        val keywords = listOf(
            keyword1,
            keyword2,
        )
        `Given search bar keyword list already populated`(keywords)

        viewModel.onKeywordSelected(keyword1)
        val selectedKeyword = keyword1.copy(isSelected = true)
        viewModel.onApplySuggestionToSelectedKeyword(query, selectedKeyword)

        val expectedKeywords = listOf(selectedKeyword, keyword2)

        `Then verify active keyword`(selectedKeyword)
        `Then verify latest keyword`(selectedKeyword)
        `Then verify SearchBarKeyword list`(expectedKeywords)
        `Then verify searchBarKeywordError`(SearchBarKeywordError.Duplicate)
        `Then verify mps state`(
            SearchBarState(
                isMpsEnabled = true,
                isAddButtonEnabled = false,
                isKeyboardDismissEnabled = false,
                shouldDisplayMpsPlaceHolder = true,
            )
        )
    }
}
