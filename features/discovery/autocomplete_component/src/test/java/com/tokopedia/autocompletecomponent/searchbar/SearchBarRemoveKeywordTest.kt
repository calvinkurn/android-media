package com.tokopedia.autocompletecomponent.searchbar

import com.tokopedia.discovery.common.constants.SearchApiConst
import org.junit.Test

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
        val expectedActiveKeyword = SearchBarKeyword(2)

        `Then verify SearchBarKeyword list`(expectedKeywords)
        `Then verify active SearchBarKeyword`(expectedActiveKeyword)

        val expectedParams = mapOf(
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
            SearchApiConst.Q1 to keyword1.keyword,
            SearchApiConst.Q2 to keyword3.keyword,
        )
        `Then verify search parameter`(expectedParams)
    }

    @Test
    fun `when last keyword removed, active keyword should change`() {
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


        `When keyword is removed`(keyword3)

        val expectedKeywords = listOf(keyword1, keyword2)
        `Then verify SearchBarKeyword list`(expectedKeywords)
        val expectedActiveKeyword = SearchBarKeyword(2)
        `Then verify active SearchBarKeyword`(expectedActiveKeyword)

        val expectedParams = mapOf(
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
            SearchApiConst.Q1 to keyword1.keyword,
            SearchApiConst.Q2 to keyword2.keyword,
        )
        `Then verify search parameter`(expectedParams)
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

        val expectedParams = mapOf(
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
            SearchApiConst.Q1 to keyword1.keyword,
            SearchApiConst.Q2 to keyword2.keyword,
            SearchApiConst.Q3 to keyword3.keyword,
        )

        `Then verify SearchBarKeyword list`(keywords)
        `Then verify search parameter`(expectedParams)
    }

    @Test
    fun `remove keyword with empty active keyword`() {
        `Given mps enabled and no coach mark should be displayed`()

        val keyword1 = SearchBarKeyword(0, "samsung")
        val keywords = listOf(
            keyword1,
        )
        `Given search bar keyword list already populated`(keywords)

        `When keyword is removed`(keyword1)

        `Then verify SearchBarKeyword list`(emptyList())
        `Then verify search parameter`(mapOf())
    }

    @Test
    fun `remove keyword with non-empty active keyword`() {
        `Given mps enabled and no coach mark should be displayed`()

        val keyword1 = SearchBarKeyword(0, "samsung")
        val keywords = listOf(
            keyword1,
        )
        val activeKeyword = SearchBarKeyword(1, "s22")
        `Given search bar keyword list already populated`(keywords)
        `Given active keyword`(activeKeyword.keyword)

        `When keyword is removed`(keyword1)

        `Then verify SearchBarKeyword list`(emptyList())
        val expectedParams = mapOf(
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_PRODUCT,
            SearchApiConst.Q to activeKeyword.keyword,
        )
        `Then verify search parameter`(expectedParams)
    }

    private fun `Given active keyword`(query : String) {
        viewModel.onQueryUpdated(query)
    }

    private fun `When keyword is removed`(
        keyword: SearchBarKeyword
    ) {
        viewModel.onKeywordRemoved(keyword)
    }
}
