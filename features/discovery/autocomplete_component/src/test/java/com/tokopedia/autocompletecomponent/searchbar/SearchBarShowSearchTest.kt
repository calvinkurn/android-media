package com.tokopedia.autocompletecomponent.searchbar

import com.tokopedia.discovery.common.constants.SearchApiConst
import org.junit.Test

internal class SearchBarShowSearchTest: SearchBarViewModelTestFixtures()  {
    @Test
    fun `product search test`() {
        val query = "samsung"
        val params = mapOf(
            SearchApiConst.Q to query,
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_PRODUCT,
        )

        `When show search called`(params)

        val expectedActiveKeyword = SearchBarKeyword(keyword = query)
        `Then verify active SearchBarKeyword`(expectedActiveKeyword)
        `Then verify search parameter`(params)
        `Then verify SearchBarKeyword list`(listOf())
    }
    
    @Test
    fun `product search with incomplete params test`() {
        val query = "samsung"
        val params = mapOf(
            SearchApiConst.Q to query,
        )

        `When show search called`(params)

        val expectedActiveKeyword = SearchBarKeyword(keyword = query)
        `Then verify active SearchBarKeyword`(expectedActiveKeyword)
        val expectedParams = mapOf(
            SearchApiConst.Q to query,
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_PRODUCT,
        )
        `Then verify search parameter`(expectedParams)
        `Then verify SearchBarKeyword list`(listOf())
    }

    @Test
    fun `product search with hint test`() {
        val query = "samsung"
        val hint = "abc"
        val params = mapOf(
            SearchApiConst.Q to query,
            SearchApiConst.HINT to hint,
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_PRODUCT,
        )

        `When show search called`(params)

        val expectedActiveKeyword = SearchBarKeyword(keyword = query)
        `Then verify active SearchBarKeyword`(expectedActiveKeyword)
        `Then verify search parameter`(params)
        `Then verify SearchBarKeyword list`(listOf())
    }

    @Test
    fun `product search with placeholder test`() {
        val query = "samsung"
        val placeholder = "abc"
        val params = mapOf(
            SearchApiConst.Q to query,
            SearchApiConst.PLACEHOLDER to placeholder,
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_PRODUCT,
        )

        `When show search called`(params)

        val expectedActiveKeyword = SearchBarKeyword(keyword = query)
        `Then verify active SearchBarKeyword`(expectedActiveKeyword)
        `Then verify search parameter`(params)
        `Then verify SearchBarKeyword list`(listOf())
    }

    @Test
    fun `mps search test`() {
        val keyword1 = SearchBarKeyword(
            keyword = "samsung",
        )
        val keyword2 = SearchBarKeyword(
            position = 1,
            keyword = "xiaomi",
        )
        val keyword3 = SearchBarKeyword(
            position = 2,
            keyword = "apple",
        )
        val params = mapOf(
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
            SearchApiConst.Q1 to keyword1.keyword,
            SearchApiConst.Q2 to keyword2.keyword,
            SearchApiConst.Q3 to keyword3.keyword,
        )

        `When show search called`(params)

        val expectedActiveKeyword = SearchBarKeyword(position = 3)
        `Then verify active SearchBarKeyword`(expectedActiveKeyword)
        `Then verify search parameter`(params)
        `Then verify SearchBarKeyword list`(listOf(
            keyword1,
            keyword2,
            keyword3,
        ))
    }

    @Test
    fun `mps search with skipped q1 params test`() {
        val keyword1 = SearchBarKeyword(
            keyword = "samsung",
        )
        val keyword2 = SearchBarKeyword(
            position = 1,
            keyword = "xiaomi",
        )
        val params = mapOf(
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
            SearchApiConst.Q2 to keyword1.keyword,
            SearchApiConst.Q3 to keyword2.keyword,
        )

        `When show search called`(params)

        val expectedActiveKeyword = SearchBarKeyword(position = 2)
        `Then verify active SearchBarKeyword`(expectedActiveKeyword)
        `Then verify search parameter`(params)
        `Then verify SearchBarKeyword list`(listOf(
            keyword1,
            keyword2,
        ))
    }

    @Test
    fun `mps search with skipped q2 params test`() {
        val keyword1 = SearchBarKeyword(
            keyword = "samsung",
        )
        val keyword2 = SearchBarKeyword(
            position = 1,
            keyword = "xiaomi",
        )
        val params = mapOf(
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
            SearchApiConst.Q1 to keyword1.keyword,
            SearchApiConst.Q3 to keyword2.keyword,
        )

        `When show search called`(params)

        val expectedActiveKeyword = SearchBarKeyword(position = 2)
        `Then verify active SearchBarKeyword`(expectedActiveKeyword)
        `Then verify search parameter`(params)
        `Then verify SearchBarKeyword list`(listOf(
            keyword1,
            keyword2,
        ))
    }

    private fun `When show search called`(params: Map<String, String>) {
        viewModel.showSearch(params)
    }
}
