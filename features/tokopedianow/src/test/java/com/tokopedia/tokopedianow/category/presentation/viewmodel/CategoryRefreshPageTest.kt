package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test

class CategoryRefreshPageTest: TokoNowCategoryMainViewModelTestFixture() {
    @Test
    fun `refreshLayout should return Unit value to trigger refresh on the layout`() {
        viewModel.refreshLayout()

        viewModel.refreshState
            .verifyValueEquals(Unit)
    }
}
