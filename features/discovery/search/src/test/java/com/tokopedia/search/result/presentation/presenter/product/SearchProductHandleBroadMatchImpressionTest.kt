package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.TopAdsComponent.BROAD_MATCH_ADS
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.BroadMatchItemViewModel
import com.tokopedia.search.result.presentation.model.BroadMatchViewModel
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val broadMatchResponseCode0Page1Position1 = "searchproduct/broadmatch/response-code-0-page-1-position-1.json"

internal class SearchProductHandleBroadMatchImpression: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val className = "SearchClassName"

    @Test
    fun `Impressed top ads broad match`() {
        `Given View already load data with broad match`()

        val broadMatchAds = findBroadMatchItemFromVisitableList(true)
        `When broad match product impressed`(broadMatchAds)

        `Then verify broad match top ads impressed`(broadMatchAds)
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

    private fun findBroadMatchItemFromVisitableList(isTopAds: Boolean): BroadMatchItemViewModel {
        val visitableList = visitableListSlot.captured

        val broadMatchViewModel = visitableList.find { it is BroadMatchViewModel } as BroadMatchViewModel

        return broadMatchViewModel.broadMatchItemViewModelList.find { it.isOrganicAds == isTopAds }!!
    }

    private fun `When broad match product impressed`(broadMatchAds: BroadMatchItemViewModel) {
        productListPresenter.onBroadMatchItemImpressed(broadMatchAds)
    }

    private fun `Then verify broad match top ads impressed`(broadMatchAds: BroadMatchItemViewModel) {
        verify {
            productListView.className

            topAdsUrlHitter.hitImpressionUrl(
                    className,
                    broadMatchAds.topAdsViewUrl,
                    broadMatchAds.id,
                    broadMatchAds.name,
                    broadMatchAds.imageUrl,
                    BROAD_MATCH_ADS
            )
        }
    }

    @Test
    fun `Impressed non top ads broad match`() {
        `Given View already load data with broad match`()

        val broadMatchNotAds = findBroadMatchItemFromVisitableList(false)
        `When broad match product impressed`(broadMatchNotAds)

        confirmVerified(topAdsUrlHitter)
    }
}