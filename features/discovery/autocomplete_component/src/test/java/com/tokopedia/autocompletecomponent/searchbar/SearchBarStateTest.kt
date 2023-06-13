package com.tokopedia.autocompletecomponent.searchbar

import org.junit.Test

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
        `Given should animate icon plus`()

        viewModel.enableMps()

        `Then verify coach mark local cache is called`()
        `Then verify mps state`(
            SearchBarState(
                isMpsEnabled = true,
                isAddButtonEnabled = true,
                isMpsAnimationEnabled = true,
            )
        )
    }
}
