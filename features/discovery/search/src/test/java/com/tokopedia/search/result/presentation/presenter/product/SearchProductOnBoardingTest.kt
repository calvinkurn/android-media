package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchConstant.ABTestRemoteConfigKey.AB_TEST_KEY_THREE_DOTS_SEARCH
import com.tokopedia.discovery.common.constants.SearchConstant.ABTestRemoteConfigKey.AB_TEST_THREE_DOTS_SEARCH_FULL_OPTIONS
import com.tokopedia.discovery.common.constants.SearchConstant.OnBoarding.THREE_DOTS_ONBOARDING_SHOWN
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
        `Configure on boarding shown`(threeDotsShown = false)
        `Given AB test for full options`()
        setUp()
        `Given view already load data`(searchProductModel)

        `When free ongkir on boarding shown`()

        `Then assert view show search on boarding`(firstProductPosition)
        `Then assert search on boarding toggled as shown`()
    }

    private fun `Configure on boarding shown`(
            threeDotsShown: Boolean
    ) {
        every { searchOnBoardingLocalCache.getBoolean(THREE_DOTS_ONBOARDING_SHOWN) } answers { threeDotsShown }
    }

    private fun `Given AB test for full options`() {
        every {
            productListView.abTestRemoteConfig.getString(AB_TEST_KEY_THREE_DOTS_SEARCH)
        } returns AB_TEST_THREE_DOTS_SEARCH_FULL_OPTIONS
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
            productListView.showOnBoarding(firstProductPosition, true)
        }
    }

    private fun `Then assert search on boarding toggled as shown`() {
        verify {
            searchOnBoardingLocalCache.putBoolean(THREE_DOTS_ONBOARDING_SHOWN, true)
            searchOnBoardingLocalCache.applyEditor()
        }
    }

    @Test
    fun `Show search on boarding after free ongkir on boarding shown - with non zero first product position`() {
        val searchProductModel = "searchproduct/globalnavwidget/show-topads-true.json".jsonToObject<SearchProductModel>()
        val firstProductPosition = 2

        `Test show on boarding`(searchProductModel, firstProductPosition)
    }

    @Test
    fun `Show search on boarding when three dots on boarding not shown and has full options`() {
        val searchProductModel = "searchproduct/common-response.json".jsonToObject<SearchProductModel>()
        val firstProductPosition = 0

        `Configure on boarding shown`(threeDotsShown = false)
        `Given AB test for full options`()
        setUp()
        `Given view already load data`(searchProductModel)

        `When free ongkir on boarding shown`()

        `Then assert view show search on boarding`(firstProductPosition)
        `Then assert search on boarding toggled as shown`()
    }

    @Test
    fun `Do not show search on boarding when three dots on boarding not shown and does not have full options`() {
        val searchProductModel = "searchproduct/common-response.json".jsonToObject<SearchProductModel>()

        `Configure on boarding shown`(threeDotsShown = false)
        `Given AB test for control variant option`()
        setUp()
        `Given view already load data`(searchProductModel)

        `When free ongkir on boarding shown`()

        `Then assert view not show search on boarding`()
    }

    private fun `Given AB test for control variant option`() {
        every {
            productListView.abTestRemoteConfig.getString(AB_TEST_KEY_THREE_DOTS_SEARCH)
        } returns ""
    }

    @Test
    fun `Do not show search on boarding if already shown`() {
        `Configure on boarding shown`(threeDotsShown = true)
        `Given view already load data`("searchproduct/common-response.json".jsonToObject())

        `When free ongkir on boarding shown`()

        `Then assert view not show search on boarding`()
    }

    private fun `Then assert view not show search on boarding`() {
        verify(exactly = 0) {
            productListView.showOnBoarding(any(), any())
        }
    }
}