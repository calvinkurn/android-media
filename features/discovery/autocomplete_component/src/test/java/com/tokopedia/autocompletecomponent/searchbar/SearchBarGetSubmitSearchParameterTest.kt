package com.tokopedia.autocompletecomponent.searchbar

import com.tokopedia.autocompletecomponent.shouldBe
import com.tokopedia.discovery.common.constants.SearchApiConst
import org.junit.Test

internal class SearchBarGetSubmitSearchParameterTest: SearchBarViewModelTestFixtures() {
    private fun `When getSubmitSearchParameter called`() : Map<String, String> {
        return viewModel.getSubmitSearchParameter()
    }

    private fun `Then verify actual params should be equal expected params`(
        actualParams : Map<String, String>,
        expectedParams : Map<String, String>,
    ) {
        actualParams shouldBe expectedParams
    }

    @Test
    fun `single keyword test`() {
        val query = "samsung"
        `Given active keyword updated`(query)

        val actualParams = `When getSubmitSearchParameter called`()

        val expectedParams = mapOf(
            SearchApiConst.Q to query,
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_PRODUCT,
        )
        `Then verify actual params should be equal expected params`(actualParams, expectedParams)
    }

    @Test
    fun `single keyword already in keyword list test`() {
        `Given mps enabled and no coach mark should be displayed`()
        val keyword1 = SearchBarKeyword(
            keyword = "apple",
        )
        `Given search bar keyword list already populated`(listOf(keyword1))

        val actualParams = `When getSubmitSearchParameter called`()

        val expectedParams = mapOf(
            SearchApiConst.Q to keyword1.keyword,
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_PRODUCT,
        )
        `Then verify actual params should be equal expected params`(actualParams, expectedParams)
    }

    @Test
    fun `single keyword plus active keyword test`() {
        `Given mps enabled and no coach mark should be displayed`()
        val keyword1 = SearchBarKeyword(
            keyword = "apple",
        )
        `Given search bar keyword list already populated`(listOf(keyword1))
        val query = "samsung"
        `Given active keyword updated`(query)

        val actualParams = `When getSubmitSearchParameter called`()

        val expectedParams = mapOf(
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
            SearchApiConst.Q1 to keyword1.keyword,
            SearchApiConst.Q2 to query,
        )
        `Then verify actual params should be equal expected params`(actualParams, expectedParams)
    }

    @Test
    fun `full keyword list test`() {
        `Given mps enabled and no coach mark should be displayed`()
        val keyword1 = SearchBarKeyword(
            keyword = "apple",
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
        `Given search bar keyword list already populated`(keywords)

        val actualParams = `When getSubmitSearchParameter called`()

        val expectedParams = mapOf(
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
            SearchApiConst.Q1 to keyword1.keyword,
            SearchApiConst.Q2 to keyword2.keyword,
            SearchApiConst.Q3 to keyword3.keyword,
        )
        `Then verify actual params should be equal expected params`(actualParams, expectedParams)
    }

    @Test
    fun `full keyword list plus additional keyword test`() {
        `Given mps enabled and no coach mark should be displayed`()
        val keyword1 = SearchBarKeyword(
            keyword = "apple",
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
        `Given search bar keyword list already populated`(keywords)
        val query = "samsung"
        `Given active keyword updated`(query)


        val actualParams = `When getSubmitSearchParameter called`()

        val expectedParams = mapOf(
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
            SearchApiConst.Q1 to keyword1.keyword,
            SearchApiConst.Q2 to keyword2.keyword,
            SearchApiConst.Q3 to keyword3.keyword,
        )
        `Then verify actual params should be equal expected params`(actualParams, expectedParams)
    }

}
