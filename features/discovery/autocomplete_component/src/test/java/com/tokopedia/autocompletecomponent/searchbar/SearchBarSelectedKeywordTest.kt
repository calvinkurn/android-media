package com.tokopedia.autocompletecomponent.searchbar

import com.tokopedia.autocompletecomponent.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SearchBarSelectedKeywordTest : SearchBarViewModelTestFixtures() {
    @Test
    fun `select keyword success`() {
        `Given mps enabled and no coach mark should be displayed`()

        val query = "samsung"
        val keyword = SearchBarKeyword(
            position = 0,
            keyword = query,
        )

        viewModel.onKeywordSelected(keyword)

        `Then verify active keyword`(keyword)
        `Then verify latest keyword`(keyword)
    }

    private fun `Then verify active keyword`(searchBarKeyword: SearchBarKeyword) {
        viewModel.activeKeyword shouldBe searchBarKeyword
    }

    private fun `Then verify latest keyword`(searchBarKeyword: SearchBarKeyword) {
        viewModel.activeKeywordLiveData.value shouldBe searchBarKeyword
    }
}
