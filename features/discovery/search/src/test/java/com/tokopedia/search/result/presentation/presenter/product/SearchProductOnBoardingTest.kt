package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchConstant.ABTestRemoteConfigKey.*
import com.tokopedia.discovery.common.constants.SearchConstant.OnBoarding.FILTER_ONBOARDING_SHOWN
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_BOTTOM_SHEET_FILTER_REVAMP
import io.mockk.every
import io.mockk.verify
import org.junit.Test

internal class SearchProductOnBoardingTest: ProductListPresenterTestFixtures() {

    @Test
    fun `Show search on boarding after free ongkir on boarding shown`() {
        `Given filter remamp is enabled from firebase`()
        `Given AB Test Remote Config returns new filter variant`()
        setUp()
        `Configure on boarding shown`(false)

        `When free ongkir on boarding shown`()

        `Then assert view show search on boarding`()
        `Then assert search on boarding toggled as shown`()
    }

    private fun `Given filter remamp is enabled from firebase`() {
        every { remoteConfig.getBoolean(ENABLE_BOTTOM_SHEET_FILTER_REVAMP, true) } answers { true }
    }

    private fun `Given AB Test Remote Config returns new filter variant`() {
        every {
            productListView.abTestRemoteConfig.getString(AB_TEST_OLD_FILER_VS_NEW_FILTER)
        } answers { AB_TEST_VARIANT_NEW_FILTER }
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
        `Given filter remamp is enabled from firebase`()
        setUp()
        `Configure on boarding shown`(true)

        `When free ongkir on boarding shown`()

        `Then assert view not show search on boarding`()
    }

    private fun `Then assert view not show search on boarding`() {
        verify(exactly = 0) {
            productListView.showOnBoarding()
        }
    }

    @Test
    fun `Do not show search on boarding if filter revamp is not enabled`() {
        `Configure on boarding shown`(false)

        `When free ongkir on boarding shown`()

        `Then assert view not show search on boarding`()
    }

    @Test
    fun `Do not show search on boarding if filter revamp AB test is OLD FILTER`() {
        `Given filter remamp is enabled from firebase`()
        `Given AB Test Remote Config returns old filter variant`()
        setUp()
        `Configure on boarding shown`(false)

        `When free ongkir on boarding shown`()

        `Then assert view not show search on boarding`()
    }

    private fun `Given AB Test Remote Config returns old filter variant`() {
        every {
            productListView.abTestRemoteConfig.getString(AB_TEST_OLD_FILER_VS_NEW_FILTER)
        } answers { AB_TEST_VARIANT_OLD_FILTER }
    }
}