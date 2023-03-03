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
            )
        )
    }

    private fun `Then verify active keyword`(searchBarKeyword: SearchBarKeyword) {
        viewModel.activeKeyword shouldBe searchBarKeyword
    }

    private fun `Then verify latest keyword`(searchBarKeyword: SearchBarKeyword) {
        viewModel.activeKeywordLiveData.value shouldBe searchBarKeyword
    }
}
