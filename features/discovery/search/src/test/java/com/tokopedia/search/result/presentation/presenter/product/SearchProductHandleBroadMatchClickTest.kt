package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.TopAdsComponent.BROAD_MATCH_ADS
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.product.broadmatch.BroadMatchDataView
import com.tokopedia.search.result.product.broadmatch.BroadMatchItemDataView
import com.tokopedia.search.result.product.broadmatch.DynamicCarouselOption
import com.tokopedia.search.result.product.broadmatch.DynamicCarouselProduct
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val broadMatchResponseCode0Page1Position1 = "searchproduct/broadmatch/response-code-0-page-1-position-1.json"
private const val dynamicProductCarousel = "searchproduct/inspirationcarousel/dynamic-product.json"
private const val dealsCarouselWithCardButton = "searchproduct/inspirationcarousel/deals-with-card-button.json"

internal class SearchProductHandleBroadMatchClick: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()

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
        `Given view already load data`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
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
            broadMatchView.trackEventClickBroadMatchItem(broadMatchItemData)
            broadMatchView.openLink(broadMatchItemData.applink, broadMatchItemData.url)
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
            broadMatchView.trackEventClickSeeMoreBroadMatch(broadMatchDataView)
            applinkModifier.modifyApplink(broadMatchDataView.applink)
            broadMatchView.openLink(any(), broadMatchDataView.url)
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
            inspirationCarouselDynamicProductView.trackDynamicProductCarouselClick(
                dynamicProductCarousel,
                carouselProductType.type,
                carouselProductType.inspirationCarouselProduct,
            )
            broadMatchView.openLink(dynamicProductCarousel.applink, dynamicProductCarousel.url)
        }

        verify(exactly = 0) {
            broadMatchView.trackEventClickBroadMatchItem(any())
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
            val carouselOptionType = dynamicProductCarousel.carouselOptionType as DynamicCarouselOption
            inspirationCarouselDynamicProductView.trackEventClickSeeMoreDynamicProductCarousel(
                dynamicProductCarousel,
                carouselOptionType.option.inspirationCarouselType,
                carouselOptionType.option,
            )
            applinkModifier.modifyApplink(dynamicProductCarousel.applink)
            broadMatchView.openLink(any(), dynamicProductCarousel.url)
        }

        verify(exactly = 0) {
            broadMatchView.trackEventClickSeeMoreBroadMatch(any())
        }
    }

    @Test
    fun `Click broad match view all card`() {
        val searchProductModel = dealsCarouselWithCardButton.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel)

        val broadMatchDataView = findBroadMatchDataViewFromVisitableList()
        `When broad match view all card click`(broadMatchDataView)

        `Then verify view interaction for click view all card dynamic carousel`(broadMatchDataView)
    }

    private fun `When broad match view all card click`(broadMatchDataView: BroadMatchDataView) {
        productListPresenter.onBroadMatchViewAllCardClicked(broadMatchDataView)
    }

    private fun `Then verify view interaction for click view all card dynamic carousel`(
        dynamicProductCarousel: BroadMatchDataView,
    ) {
        verify {
            val carouselOptionType = dynamicProductCarousel.carouselOptionType as DynamicCarouselOption
            inspirationCarouselDynamicProductView.trackEventClickSeeMoreDynamicProductCarousel(
                dynamicProductCarousel,
                carouselOptionType.option.inspirationCarouselType,
                carouselOptionType.option,
            )
            applinkModifier.modifyApplink(any())
            broadMatchView.openLink(any(), dynamicProductCarousel.url)
        }

        verify(exactly = 0) {
            broadMatchView.trackEventClickSeeMoreBroadMatch(any())
        }
    }
}
