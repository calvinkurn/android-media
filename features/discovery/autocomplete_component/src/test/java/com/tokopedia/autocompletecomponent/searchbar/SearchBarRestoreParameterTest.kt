package com.tokopedia.autocompletecomponent.searchbar

import com.tokopedia.autocompletecomponent.shouldBe
import com.tokopedia.discovery.common.constants.SearchApiConst
import org.junit.Test

internal class SearchBarRestoreParameterTest : SearchBarViewModelTestFixtures() {
    @Test
    fun `restore search parameter`() {
        val q1 = "samsung"
        val q2 = "apple"
        val q3 = "xiaomi"
        val params = mapOf(
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
            SearchApiConst.Q1 to q1,
            SearchApiConst.Q2 to q2,
            SearchApiConst.Q3 to q3,
        )
        val activeKeyword = SearchBarKeyword(
            position = 3,
        )

        `When search parameter restored`(params, activeKeyword)

        val expectedKeywords = listOf(
            SearchBarKeyword(keyword = q1),
            SearchBarKeyword(position = 1, q2),
            SearchBarKeyword(position = 2, q3),
        )

        `Then verify restored active keyword`(activeKeyword)
        `Then verify search parameter`(params)
        `Then verify restored search bar keywords`(expectedKeywords)
    }

    private fun `When search parameter restored`(
        params: Map<String, String>,
        activeKeyword: SearchBarKeyword
    ) {
        viewModel.restoreSearchParameter(params, activeKeyword)
    }

    private fun `Then verify restored active keyword`(expectedActiveKeyword: SearchBarKeyword) {
        viewModel.activeKeyword shouldBe expectedActiveKeyword
        viewModel.activeKeywordLiveData.value shouldBe expectedActiveKeyword
    }

    private fun `Then verify restored search bar keywords`(
        expectedKeywords: List<SearchBarKeyword>
    ) {
        viewModel.searchBarKeywords.value?.forEachIndexed { index, searchBarKeyword ->
            searchBarKeyword shouldBe expectedKeywords[index]
        }
    }
}
