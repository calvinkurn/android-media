package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.TopAdsComponent.BROAD_MATCH_ADS
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val broadMatchResponseCode0Page1Position1 = "searchproduct/broadmatch/response-code-0-page-1-position-1.json"

internal class SearchProductHandleBroadMatchClick: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val className = "SearchClassName"

    @Test
    fun `Click broad match item`() {
        `Given View already load data with broad match`()

        val broadMatchItem = findBroadMatchItemFromVisitableList(false)
        `When broad match product click`(broadMatchItem)

        `Then verify view interaction for click broad match`(broadMatchItem)
        confirmVerified(topAdsUrlHitter)
    }

    private fun `Given View already load data with broad match`() {
        val searchProductModel = broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given class name`()
        `Given view already load data`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given class name`() {
        every { productListView.className } returns className
    }

    private fun `Given view already load data`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        productListPresenter.loadData(mapOf())
    }

    private fun findBroadMatchItemFromVisitableList(isTopAds: Boolean): BroadMatchItemDataView {
        val visitableList = visitableListSlot.captured

        val broadMatchViewModel = visitableList.find { it is BroadMatchDataView } as BroadMatchDataView

        return broadMatchViewModel.broadMatchItemDataViewList.find { it.isOrganicAds == isTopAds }!!
    }

    private fun `When broad match product click`(broadMatchAdsData: BroadMatchItemDataView) {
        productListPresenter.onBroadMatchItemClick(broadMatchAdsData)
    }

    private fun `Then verify view interaction for click broad match`(broadMatchItemData: BroadMatchItemDataView) {
        verify {
            productListView.trackEventClickBroadMatchItem(broadMatchItemData)
            productListView.redirectionStartActivity(broadMatchItemData.applink, broadMatchItemData.url)
        }
    }

    @Test
    fun `Click top ads broad match item`() {
        `Given View already load data with broad match`()

        val broadMatchItem = findBroadMatchItemFromVisitableList(true)
        `When broad match product click`(broadMatchItem)

        `Then verify view interaction for click broad match`(broadMatchItem)

        `Then verify broad match top ads clicked`(broadMatchItem)
    }

    private fun `Then verify broad match top ads clicked`(broadMatchItemData: BroadMatchItemDataView) {
        verify {
            productListView.className

            topAdsUrlHitter.hitClickUrl(
                    className,
                    broadMatchItemData.topAdsClickUrl,
                    broadMatchItemData.id,
                    broadMatchItemData.name,
                    broadMatchItemData.imageUrl,
                    BROAD_MATCH_ADS
            )
        }
    }
}