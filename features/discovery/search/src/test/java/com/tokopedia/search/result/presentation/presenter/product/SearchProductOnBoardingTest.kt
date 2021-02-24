package com.tokopedia.search.result.presentation.presenter.product

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
        `Configure on boarding shown`(shouldShow = true)
        `Given view already load data`(searchProductModel)

        `When free ongkir on boarding shown`(firstProductPosition)

        `Then assert view show search on boarding`(firstProductPosition)
    }

    private fun `Configure on boarding shown`(
            shouldShow: Boolean
    ) {
        every { searchCoachMarkLocalCache.shouldShowBoeCoachmark() } answers { shouldShow }
    }

    private fun `Given view already load data`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        productListPresenter.loadData(mapOf())
    }

    private fun `When free ongkir on boarding shown`(position: Int) {
        productListPresenter.showBOELabelOnBoarding(position)
    }

    private fun `Then assert view show search on boarding`(firstProductPosition: Int) {
        verify {
            productListView.showOnBoarding(firstProductPosition)
        }
    }

    @Test
    fun `Show search on boarding after free ongkir on boarding shown - with no BOE product found`() {
        val searchProductModel = "searchproduct/globalnavwidget/show-topads-true.json".jsonToObject<SearchProductModel>()

        `Configure on boarding shown`(shouldShow = true)
        `Given view already load data`(searchProductModel)

        `When free ongkir on boarding shown`(0)

        `Then assert view not show search on boarding`()
    }

    @Test
    fun `Do not show search on boarding if already shown`() {
        `Configure on boarding shown`(shouldShow = false)
        `Given view already load data`("searchproduct/common-response.json".jsonToObject())

        `When free ongkir on boarding shown`(0)

        `Then assert view not show search on boarding`()
    }

    private fun `Then assert view not show search on boarding`() {
        verify(exactly = 0) {
            productListView.showOnBoarding(any())
        }
    }
}