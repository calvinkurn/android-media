package com.tokopedia.autocompletecomponent.searchbar

import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.discovery.common.constants.SearchApiConst
import org.junit.Test

internal class SearchBarSelectInitialStateItemTest : SearchBarViewModelTestFixtures() {
    @Test
    fun `select initial state item success`() {
        val keyword1 = SearchBarKeyword(
            keyword = "iphone",
        )
        `Given mps enabled and no coach mark should be displayed`()
        `Given search bar keyword list already populated`(listOf(keyword1))

        val query = "samsung"
        val selectedItem = BaseItemInitialStateSearch(
            title = query
        )

        `When initial state item selected`(selectedItem)

        val expectedParams = mapOf(
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
            SearchApiConst.Q1 to keyword1.keyword,
            SearchApiConst.Q2 to query,
        )
        `Then verify search parameter`(
            expectedParams
        )
        val expectedActiveKeyword = SearchBarKeyword(position = 2)
        `Then verify active SearchBarKeyword`(
            expectedActiveKeyword
        )
        val expectedKeywords = listOf(
            keyword1,
            SearchBarKeyword(position = 1, keyword = query)
        )
        `Then verify SearchBarKeyword list`(expectedKeywords)
    }

    @Test
    fun `select initial state item on full keyword list`() {
        val keyword1 = SearchBarKeyword(
            keyword = "iphone",
        )
        val keyword2 = SearchBarKeyword(
            position = 1,
            keyword = "samsung s22",
        )
        val keyword3 = SearchBarKeyword(
            position = 2,
            keyword = "xiaomi",
        )
        val keywords = listOf(
            keyword1,
            keyword2,
            keyword3,
        )
        `Given mps enabled and no coach mark should be displayed`()
        `Given search bar keyword list already populated`(keywords)

        val query = "samsung"
        val selectedItem = BaseItemInitialStateSearch(
            title = query
        )

        `When initial state item selected`(selectedItem)

        val expectedParams = mapOf(
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
            SearchApiConst.Q1 to keyword1.keyword,
            SearchApiConst.Q2 to keyword2.keyword,
            SearchApiConst.Q3 to keyword3.keyword,
        )
        `Then verify search parameter`(
            expectedParams
        )
        val expectedActiveKeyword = SearchBarKeyword(position = 3)
        `Then verify active SearchBarKeyword`(
            expectedActiveKeyword
        )
        `Then verify SearchBarKeyword list`(keywords)
    }

    @Test
    fun `select initial state item with keyword already in keyword list`() {
        val keyword1 = SearchBarKeyword(
            keyword = "iphone",
        )
        val keyword2 = SearchBarKeyword(
            position = 1,
            keyword = "samsung",
        )
        val keyword3 = SearchBarKeyword(
            position = 2,
            keyword = "xiaomi",
        )
        val keywords = listOf(
            keyword1,
            keyword2,
            keyword3,
        )
        `Given mps enabled and no coach mark should be displayed`()
        `Given search bar keyword list already populated`(keywords)

        val query = "samsung"
        val selectedItem = BaseItemInitialStateSearch(
            title = query
        )

        `When initial state item selected`(selectedItem)

        val expectedParams = mapOf(
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
            SearchApiConst.Q1 to keyword1.keyword,
            SearchApiConst.Q2 to keyword2.keyword,
            SearchApiConst.Q3 to keyword3.keyword,
        )
        `Then verify search parameter`(
            expectedParams
        )
        val expectedActiveKeyword = SearchBarKeyword(position = 3)
        `Then verify active SearchBarKeyword`(
            expectedActiveKeyword
        )
        `Then verify SearchBarKeyword list`(keywords)
    }

    private fun `When initial state item selected`(
        item: BaseItemInitialStateSearch
    ) {
        viewModel.onInitialStateItemSelected(item)
    }


}
