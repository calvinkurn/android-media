package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchConstant.OnBoarding.FILTER_ONBOARDING_SHOWN
import io.mockk.every
import io.mockk.verify
import org.junit.Test

internal class SearchProductOnBoardingTest: ProductListPresenterTestFixtures() {

    @Test
    fun `Show search on boarding after free ongkir on boarding shown`() {
        `Configure on boarding shown`(false)

        `When free ongkir on boarding shown`()

        `Then assert view show search on boarding`()
        `Then assert search on boarding toggled as shown`()
    }

    private fun `Configure on boarding shown`(isShown: Boolean) {
        every { searchOnBoardingLocalCache.getBoolean(FILTER_ONBOARDING_SHOWN) } answers { isShown }
    }

    private fun `When free ongkir on boarding shown`() {
        productListPresenter.onFreeOngkirOnBoardingShown()
    }

    private fun `Then assert view show search on boarding`() {
        verify {
            productListView.showOnBoarding()
        }
    }

    private fun `Then assert search on boarding toggled as shown`() {
        verify {
            searchOnBoardingLocalCache.putBoolean(FILTER_ONBOARDING_SHOWN, true)
            searchOnBoardingLocalCache.applyEditor()
        }
    }

    @Test
    fun `Do not show search on boarding if already shown`() {
        `Configure on boarding shown`(true)

        `When free ongkir on boarding shown`()

        `Then assert view not show search on boarding`()
    }

    private fun `Then assert view not show search on boarding`() {
        verify(exactly = 0) {
            productListView.showOnBoarding()
        }
    }
}