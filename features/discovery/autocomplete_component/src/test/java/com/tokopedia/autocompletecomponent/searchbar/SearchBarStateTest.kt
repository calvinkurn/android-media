package com.tokopedia.autocompletecomponent.searchbar

import org.junit.Test

internal class SearchBarStateTest : SearchBarViewModelTestFixtures() {

    @Test
    fun `enable mps success`() {
        `Given no coach mark should be displayed`()
        `Given should animate icon plus`()

        viewModel.showMps()

        `Then verify coach mark local cache is called`()
        `Then verify mps state`(
            SearchBarState(
                isAddButtonEnabled = true,
                isMpsAnimationEnabled = true,
            )
        )
    }
}
