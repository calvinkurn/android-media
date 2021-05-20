package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.POWER_MERCHANT_PRO_POP_UP
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.POWER_MERCHANT_PRO_POP_UP_NOT_SHOW
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.POWER_MERCHANT_PRO_POP_UP_SHOW
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

internal class SearchProductHandlePowerMerchantProPopUpTest: ProductListPresenterTestFixtures() {

    @Test
    fun `Do not show Power Merchant Pro Pop Up for control`() {
        `Given AB Test will not show Power Merchant Pro Pop Up for control`()
        setUp()

        val searchProductModel = "searchproduct/common-response.json".jsonToObject<SearchProductModel>()
        `Configure on boarding shown`(shouldShow = true)
        `Given use case returns data`(searchProductModel)

        `When view loads data`()

        `Then assert view not show search on boarding`()
    }

    private fun `Given AB Test will not show Power Merchant Pro Pop Up for control`() {
        every {
            productListView.abTestRemoteConfig?.getString(POWER_MERCHANT_PRO_POP_UP, POWER_MERCHANT_PRO_POP_UP_NOT_SHOW)
        } returns POWER_MERCHANT_PRO_POP_UP_NOT_SHOW
    }

    @Test
    fun `Show Power Merchant Pro Pop Up`() {
        `Given AB Test will show Power Merchant Pro Pop Up`()
        setUp()

        val searchProductModel = "searchproduct/common-response.json".jsonToObject<SearchProductModel>()
        `Configure on boarding shown`(shouldShow = true)
        `Given use case returns data`(searchProductModel)

        `When view loads data`()

        `Then assert view show search on boarding`()
    }

    private fun `Given AB Test will show Power Merchant Pro Pop Up`() {
        every {
            productListView.abTestRemoteConfig?.getString(POWER_MERCHANT_PRO_POP_UP, POWER_MERCHANT_PRO_POP_UP_NOT_SHOW)
        } returns POWER_MERCHANT_PRO_POP_UP_SHOW
    }

    private fun `Configure on boarding shown`(
            shouldShow: Boolean
    ) {
        every { searchCoachMarkLocalCache.shouldShowSearchPMProPopUp() } answers { shouldShow }
    }

    private fun `Given use case returns data`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When view loads data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `Then assert view show search on boarding`() {
        verify {
            productListView.showPowerMerchantProPopUp()
        }
    }

    @Test
    fun `Do not show Power Merchant Pop Up if already shown`() {
        `Given AB Test will show Power Merchant Pro Pop Up`()
        setUp()

        val searchProductModel = "searchproduct/common-response.json".jsonToObject<SearchProductModel>()
        `Configure on boarding shown`(shouldShow = false)
        `Given use case returns data`(searchProductModel)

        `When view loads data`()

        `Then assert view not show search on boarding`()
    }

    private fun `Then assert view not show search on boarding`() {
        verify(exactly = 0) {
            productListView.showPowerMerchantProPopUp()
        }
    }
}