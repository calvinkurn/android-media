package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

internal class SearchProductHandlePowerMerchantProPopUpTest: ProductListPresenterTestFixtures() {

    @Test
    fun `Show Power Merchant Pro Pop Up`() {
        val searchProductModel = "searchproduct/common-response.json".jsonToObject<SearchProductModel>()
        `Configure on boarding shown`(shouldShow = true)
        `Given use case returns data`(searchProductModel)

        `When view loads data`()

        `Then assert view show search on boarding`()
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