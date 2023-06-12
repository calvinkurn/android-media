package com.tokopedia.autocompletecomponent.searchbar

import org.junit.Test

internal class SearchBarAddKeywordTest : SearchBarViewModelTestFixtures() {

    @Test
    fun `add keyword success`() {
        `Given mps enabled and no coach mark should be displayed`()

        val query = "samsung"
        val keyword = SearchBarKeyword(
            position = 0,
            keyword = query,
        )
        val keywords = listOf(keyword)

        `When keyword is added`(query)

        `Then verify SearchBarKeyword list`(keywords)
        `Then verify searchBarKeywordError`(null)
    }

    @Test
    fun `add keyword success first time`() {
        `Given coach mark local cache mark displayed will just run`()
        `Given should show added keyword coach mark`()
        `Given should not show icon plus coach mark`()
        `Given should animate icon plus`()
        `Given mps is enabled`()

        val query = "samsung"
        val keyword = SearchBarKeyword(
            position = 0,
            keyword = query,
            shouldShowCoachMark = true,
        )
        val keywords = listOf(keyword)

        `When keyword is added`(query)

        `Then verify SearchBarKeyword list`(keywords)
        `Then verify searchBarKeywordError`(null)
    }

    @Test
    fun `empty keyword should not be added to list`() {
        `Given mps enabled and no coach mark should be displayed`()

        val query = ""

        `When keyword is added`(query)

        `Then verify SearchBarKeyword list`(emptyList())
        `Then verify searchBarKeywordError`(SearchBarKeywordError.Empty)
    }

    @Test
    fun `add keyword already in the list, keyword should not be added`() {
        `Given mps enabled and no coach mark should be displayed`()

        val query = "samsung"
        val keyword = SearchBarKeyword(
            position = 0,
            keyword = query,
        )
        val keywords = listOf(keyword)
        `Given search bar keyword list already populated`(keywords)

        `When keyword is added`(query)

        `Then verify SearchBarKeyword list`(keywords)
        `Then verify searchBarKeywordError`(SearchBarKeywordError.Duplicate)
    }

    @Test
    fun `add keyword with different case that already in the list, keyword should not be added`() {
        `Given mps enabled and no coach mark should be displayed`()

        val query = "Samsung"
        val keyword = SearchBarKeyword(
            position = 0,
            keyword = "samsung",
        )
        val keywords = listOf(keyword)
        `Given search bar keyword list already populated`(keywords)

        `When keyword is added`(query)

        `Then verify SearchBarKeyword list`(keywords)
        `Then verify searchBarKeywordError`(SearchBarKeywordError.Duplicate)
    }

    @Test
    fun `add keyword when list already full, keyword should not be added`() {
        `Given mps enabled and no coach mark should be displayed`()

        val query = "samsung galaxy"
        val keyword1 = SearchBarKeyword(0, "samsung")
        val keyword2 = SearchBarKeyword(1, "iphone")
        val keyword3 = SearchBarKeyword(2, "asus")
        val keywords = listOf(
            keyword1,
            keyword2,
            keyword3,
        )
        `Given search bar keyword list already populated`(keywords)

        `When keyword is added`(query)

        `Then verify SearchBarKeyword list`(keywords)
        `Then verify searchBarKeywordError`(null)
    }

    private fun `When keyword is added`(
        query: String
    ) {
        viewModel.onQueryUpdated(query)
        viewModel.onKeywordAdded(query)
    }
}
