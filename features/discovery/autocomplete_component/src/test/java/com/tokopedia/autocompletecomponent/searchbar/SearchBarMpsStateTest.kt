package com.tokopedia.autocompletecomponent.searchbar

import com.tokopedia.autocompletecomponent.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SearchBarMpsStateTest : SearchBarViewModelTestFixtures() {
    @Test
    fun `disable mps success`() {
        viewModel.disableMps()

        `Then verify coach mark local cache is not called`()
        `Then verify mps state`(SearchBarMpsState())
    }

    @Test
    fun `enable mps success`() {
        `Given no coach mark should be displayed`()

        viewModel.enableMps()

        `Then verify coach mark local cache is called`()
        `Then verify mps state`(SearchBarMpsState(isMpsEnabled = true))
    }

    private fun `Then verify mps state`(mpsState: SearchBarMpsState) {
        viewModel.mpsStateLiveData.value shouldBe mpsState
    }
}
