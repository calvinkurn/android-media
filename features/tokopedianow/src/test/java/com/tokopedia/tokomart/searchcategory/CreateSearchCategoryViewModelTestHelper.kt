package com.tokopedia.tokomart.searchcategory

import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.NAVIGATION_EXP_TOP_NAV
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.NAVIGATION_VARIANT_OLD
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.NAVIGATION_VARIANT_REVAMP
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokomart.searchcategory.utils.ABTestPlatformWrapper
import io.mockk.every
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CreateSearchCategoryViewModelTestHelper(
        private val abTestPlatformWrapper: ABTestPlatformWrapper,
        private val callback: Callback,
) {

    fun `test has global menu for variant navigation revamp`() {
        `Given AB Test Navigation Exp Variant`(NAVIGATION_VARIANT_REVAMP)

        callback.`When create view model`()

        `Then assert has global menu`(true)
    }

    private fun `Given AB Test Navigation Exp Variant`(variant: String) {
        every {
            abTestPlatformWrapper.getABTestRemoteConfig()?.getString(
                    NAVIGATION_EXP_TOP_NAV,
                    NAVIGATION_VARIANT_OLD
            )
        } returns (variant)
    }

    private fun `Then assert has global menu`(expectedHasGlobalMenu: Boolean) {
        val viewModel = callback.getViewModel()

        assertThat(viewModel.hasGlobalMenu, shouldBe(expectedHasGlobalMenu))
    }

    fun `test has global menu for variant old navigation`() {
        `Given AB Test Navigation Exp Variant`(NAVIGATION_VARIANT_OLD)

        callback.`When create view model`()

        `Then assert has global menu`(false)
    }

    interface Callback {
        fun `When create view model`()

        fun getViewModel(): BaseSearchCategoryViewModel
    }
}