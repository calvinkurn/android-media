package com.tokopedia.autocompletecomponent.searchbar

import com.tokopedia.autocompletecomponent.shouldBe
import com.tokopedia.discovery.common.constants.SearchApiConst
import org.junit.Test

internal class SearchBarUpdateKeywordTest : SearchBarViewModelTestFixtures() {
    @Test
    fun `update keyword success`() {
        `Given mps enabled and no coach mark should be displayed`()

        val query = "samsung"
        val keyword = SearchBarKeyword(
            position = 0,
            keyword = query,
        )

        viewModel.onQueryUpdated(query)

        val expectedParams = mapOf(
            SearchApiConst.Q to query,
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_PRODUCT,
        )

        `Then verify active keyword`(keyword)
        `Then verify search parameter`(expectedParams)
    }

    @Test
    fun `update with empty keyword`() {
        `Given mps enabled and no coach mark should be displayed`()

        val query = ""
        val keyword = SearchBarKeyword(
            position = 0,
            keyword = query,
        )

        viewModel.onQueryUpdated(query)

        `Then verify active keyword`(keyword)
        `Then verify search parameter`(mapOf())
    }

    @Test
    fun `update with blank keyword`() {
        `Given mps enabled and no coach mark should be displayed`()

        val query = "   "
        val keyword = SearchBarKeyword(
            position = 0,
            keyword = query,
        )

        viewModel.onQueryUpdated(query)

        `Then verify active keyword`(keyword)
        `Then verify search parameter`(mapOf())
    }

    private fun `Then verify active keyword`(searchBarKeyword: SearchBarKeyword) {
        viewModel.activeKeyword shouldBe searchBarKeyword
    }
}
