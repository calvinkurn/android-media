package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchConstant.OnBoarding.BEBAS_ONGKIR_EXTRA_ONBOARDING_SHOWN
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

internal class SearchProductOnBoardingTest: ProductListPresenterTestFixtures() {

    @Test
    fun `Show search on boarding after free ongkir on boarding shown`() {
        val searchProductModel = "searchproduct/common-response.json".jsonToObject<SearchProductModel>()
        val firstProductPosition = 0

        `Test show on boarding`(searchProductModel, firstProductPosition)
    }

    private fun `Test show on boarding`(searchProductModel: SearchProductModel, firstProductPosition: Int) {
        `Configure on boarding shown`(boeShown = false)
        `Given view already load data`(searchProductModel)

        `When free ongkir on boarding shown`()

        `Then assert view show search on boarding`(firstProductPosition)
        `Then assert search on boarding toggled as shown`()
    }

    private fun `Configure on boarding shown`(
            boeShown: Boolean
    ) {
        every { searchOnBoardingLocalCache.getBoolean(BEBAS_ONGKIR_EXTRA_ONBOARDING_SHOWN) } answers { boeShown }
    }

    private fun `Given view already load data`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        productListPresenter.loadData(mapOf())
    }

    private fun `When free ongkir on boarding shown`() {
        productListPresenter.onFreeOngkirOnBoardingShown()
    }

    private fun `Then assert view show search on boarding`(firstProductPosition: Int) {
        verify {
            productListView.showOnBoarding(firstProductPosition)
        }
    }

    private fun `Then assert search on boarding toggled as shown`() {
        verify {
            searchOnBoardingLocalCache.putBoolean(BEBAS_ONGKIR_EXTRA_ONBOARDING_SHOWN, true)
            searchOnBoardingLocalCache.applyEditor()
        }
    }

    @Test
    fun `Show search on boarding after free ongkir on boarding shown - with no BOE product found`() {
        val searchProductModel = "searchproduct/globalnavwidget/show-topads-true.json".jsonToObject<SearchProductModel>()

        `Configure on boarding shown`(boeShown = false)
        `Given view already load data`(searchProductModel)

        `When free ongkir on boarding shown`()

        `Then assert view not show search on boarding`()
    }

    @Test
    fun `Do not show search on boarding if already shown`() {
        `Configure on boarding shown`(boeShown = true)
        `Given view already load data`("searchproduct/common-response.json".jsonToObject())

        `When free ongkir on boarding shown`()

        `Then assert view not show search on boarding`()
    }

    private fun `Then assert view not show search on boarding`() {
        verify(exactly = 0) {
            productListView.showOnBoarding(any())
            searchOnBoardingLocalCache.putBoolean(BEBAS_ONGKIR_EXTRA_ONBOARDING_SHOWN, true)
            searchOnBoardingLocalCache.applyEditor()
        }
    }
}