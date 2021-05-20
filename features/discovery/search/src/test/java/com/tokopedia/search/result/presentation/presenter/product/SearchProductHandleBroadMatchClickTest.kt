package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.TopAdsComponent.BROAD_MATCH_ADS
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.DynamicCarouselProduct
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val broadMatchResponseCode0Page1Position1 = "searchproduct/broadmatch/response-code-0-page-1-position-1.json"
private const val dynamicProductCarousel = "searchproduct/inspirationcarousel/dynamic-product.json"

internal class SearchProductHandleBroadMatchClick: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val className = "SearchClassName"

    @Test
    fun `Click broad match item`() {
        val searchProductModel = broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel)

        val broadMatchItem = findBroadMatchItemFromVisitableList(false)
        `When broad match product click`(broadMatchItem)

        `Then verify view interaction for click broad match`(broadMatchItem)
        confirmVerified(topAdsUrlHitter)
    }

    private fun `Given View already load data with broad match`(searchProductModel: SearchProductModel) {
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
        val searchProductModel = broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel)

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

    @Test
    fun `Click broad match see more`() {
        val searchProductModel = broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel)

        val broadMatchDataView = findBroadMatchDataViewFromVisitableList()
        `When broad match see more click`(broadMatchDataView)

        `Then verify view interaction for click see more broad match`(broadMatchDataView)
        confirmVerified(topAdsUrlHitter)
    }

    private fun findBroadMatchDataViewFromVisitableList(): BroadMatchDataView {
        val visitableList = visitableListSlot.captured

        return visitableList.find { it is BroadMatchDataView } as BroadMatchDataView
    }

    private fun `When broad match see more click`(broadMatchDataView: BroadMatchDataView) {
         productListPresenter.onBroadMatchSeeMoreClick(broadMatchDataView)
    }

    private fun `Then verify view interaction for click see more broad match`(broadMatchDataView: BroadMatchDataView) {
        verify {
            productListView.trackEventClickSeeMoreBroadMatch(broadMatchDataView)
            productListView.modifyApplinkToSearchResult(broadMatchDataView.applink)
            productListView.redirectionStartActivity(any(), broadMatchDataView.url)
        }
    }

    @Test
    fun `Click dynamic product carousel broad match`() {
        val searchProductModel = dynamicProductCarousel.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel)

        val dynamicProductCarousel = findBroadMatchItemFromVisitableList(false)
        `When broad match product click`(dynamicProductCarousel)

        `Then verify view interaction for click dynamic product carousel`(dynamicProductCarousel)
        confirmVerified(topAdsUrlHitter)
    }

    private fun `Then verify view interaction for click dynamic product carousel`(dynamicProductCarousel: BroadMatchItemDataView) {
        verify {
            val carouselProductType = dynamicProductCarousel.carouselProductType as DynamicCarouselProduct
            productListView.trackDynamicProductCarouselClick(dynamicProductCarousel, carouselProductType.type)
            productListView.redirectionStartActivity(dynamicProductCarousel.applink, dynamicProductCarousel.url)
        }

        verify(exactly = 0) {
            productListView.trackEventClickBroadMatchItem(any())
        }
    }

    @Test
    fun `Click dynamic product carousel see more`() {
        val searchProductModel = dynamicProductCarousel.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel)

        val broadMatchDataView = findBroadMatchDataViewFromVisitableList()
        `When broad match see more click`(broadMatchDataView)

        `Then verify view interaction for click see more dynamic product carousel`(broadMatchDataView)
        confirmVerified(topAdsUrlHitter)
    }

    private fun `Then verify view interaction for click see more dynamic product carousel`(dynamicProductCarousel: BroadMatchDataView) {
        verify {
            val carouselProductType = dynamicProductCarousel.broadMatchItemDataViewList.first().carouselProductType as DynamicCarouselProduct
            productListView.trackEventClickSeeMoreDynamicProductCarousel(dynamicProductCarousel, carouselProductType.type)
            productListView.modifyApplinkToSearchResult(dynamicProductCarousel.applink)
            productListView.redirectionStartActivity(any(), dynamicProductCarousel.url)
        }

        verify(exactly = 0) {
            productListView.trackEventClickSeeMoreBroadMatch(any())
        }
    }
}