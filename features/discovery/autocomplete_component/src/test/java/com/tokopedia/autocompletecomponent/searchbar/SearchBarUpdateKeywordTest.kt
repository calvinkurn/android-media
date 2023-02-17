package com.tokopedia.autocompletecomponent.searchbar

import com.tokopedia.autocompletecomponent.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
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

        `Then verify active keyword`(keyword)
    }

    private fun `Then verify active keyword`(searchBarKeyword: SearchBarKeyword) {
        viewModel.activeKeyword shouldBe searchBarKeyword
    }
}
