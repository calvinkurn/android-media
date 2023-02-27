package com.tokopedia.autocompletecomponent.searchbar

import com.tokopedia.autocompletecomponent.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SearchBarStateTest : SearchBarViewModelTestFixtures() {
    @Test
    fun `disable mps success`() {
        viewModel.disableMps()

        `Then verify coach mark local cache is not called`()
        `Then verify mps state`(SearchBarState())
    }

    @Test
    fun `enable mps success`() {
        `Given no coach mark should be displayed`()

        viewModel.enableMps()

        `Then verify coach mark local cache is called`()
        `Then verify mps state`(SearchBarState(
            isMpsEnabled = true,
            isAddButtonEnabled = true,
        ))
    }
}
