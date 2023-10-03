package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.product.changeview.ViewType
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val listViewExperimentJSON = "searchproduct/listviewexperiment/listview-experiment.json"

internal class SearchProductListViewExperimentTest: ProductListPresenterTestFixtures() {

    @Test
    fun `show onboarding when view type is list view and coachmark not shown yet`() {
        val searchProductModel = listViewExperimentJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given view should show view type coachmark`()

        `When load data`()

        `Then verify view show view type on boarding`()
    }

    private fun `Given Search Product API will return SearchProductModel`(
        searchProductModel: SearchProductModel
    ) {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given view should show view type coachmark`() {
        every { searchCoachMarkLocalCache.shouldShowViewTypeCoachmark() } returns true
    }

    private fun `When load data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `Then verify view show view type on boarding`() {
        verify {
            productListView.enableProductViewTypeOnBoarding()
        }
    }

    @Test
    fun `set default view to list view when view type is list view`() {
        val searchProductModel = listViewExperimentJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given view should show view type coachmark`()

        `When load data`()

        `Then verify set default layout type to list view`()
    }

    private fun `Then verify set default layout type to list view`() {
        verify {
            productListView.setDefaultLayoutType(ViewType.LIST.value)
        }
    }

    @Test
    fun `do not show onboarding when view type is list view and coachmark is already shown`() {
        val searchProductModel = listViewExperimentJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given view already show view type coachmark`()

        `When load data`()

        `Then verify view does not show view type on boarding`()
    }

    private fun `Given view already show view type coachmark`() {
        every { searchCoachMarkLocalCache.shouldShowViewTypeCoachmark() } returns false
    }

    private fun `Then verify view does not show view type on boarding`() {
        verify (exactly = 0) {
            productListView.enableProductViewTypeOnBoarding()
        }
    }

    @Test
    fun `do not show onboarding when view type is not list view`() {
        val searchProductModel = searchProductCommonResponseJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given view should show view type coachmark`()

        `When load data`()

        `Then verify view does not show view type on boarding`()
    }
}
