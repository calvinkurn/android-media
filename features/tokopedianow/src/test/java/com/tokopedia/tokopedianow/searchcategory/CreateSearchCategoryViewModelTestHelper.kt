package com.tokopedia.tokopedianow.searchcategory

import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CreateSearchCategoryViewModelTestHelper(
        private val callback: Callback,
) {

    fun `test has global menu for variant navigation revamp`() {

        callback.`When create view model`()

        `Then assert has global menu`(true)
    }

    private fun `Then assert has global menu`(expectedHasGlobalMenu: Boolean) {
        val viewModel = callback.getViewModel()

        assertThat(viewModel.hasGlobalMenu, shouldBe(expectedHasGlobalMenu))
    }

    fun `test has global menu for variant old navigation`() {

        callback.`When create view model`()

        `Then assert has global menu`(true)
    }

    interface Callback {
        fun `When create view model`()

        fun getViewModel(): BaseSearchCategoryViewModel
    }
}
