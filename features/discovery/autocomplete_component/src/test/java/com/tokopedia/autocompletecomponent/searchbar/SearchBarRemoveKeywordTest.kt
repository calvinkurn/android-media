package com.tokopedia.autocompletecomponent.searchbar

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SearchBarRemoveKeywordTest : SearchBarViewModelTestFixtures() {

    @Test
    fun `remove keyword success`() {
        `Given mps enabled and no coach mark should be displayed`()

        val keyword1 = SearchBarKeyword(0, "samsung")
        val keyword2 = SearchBarKeyword(1, "iphone")
        val keyword3 = SearchBarKeyword(2, "asus")
        val keywords = listOf(
            keyword1,
            keyword2,
            keyword3,
        )
        `Given search bar keyword list already populated`(keywords)

        `When keyword is removed`(keyword2)

        val expectedKeywords = listOf(
            keyword1,
            keyword3.copy(position = 1),
        )

        `Then verify SearchBarKeyword list`(expectedKeywords)
    }

    @Test
    fun `when removed keyword not found, keyword list should not changed`() {
        `Given mps enabled and no coach mark should be displayed`()

        val keyword1 = SearchBarKeyword(0, "samsung")
        val keyword2 = SearchBarKeyword(1, "iphone")
        val keyword3 = SearchBarKeyword(2, "asus")
        val keywords = listOf(
            keyword1,
            keyword2,
            keyword3,
        )
        `Given search bar keyword list already populated`(keywords)

        val keyword4 = SearchBarKeyword(3, "apple")

        `When keyword is removed`(keyword4)

        `Then verify SearchBarKeyword list`(keywords)
    }

    private fun `When keyword is removed`(
        keyword: SearchBarKeyword
    ) {
        viewModel.onKeywordRemoved(keyword)
    }
}
