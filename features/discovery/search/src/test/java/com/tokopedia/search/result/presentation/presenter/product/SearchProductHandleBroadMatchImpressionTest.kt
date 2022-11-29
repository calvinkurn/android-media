package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.TopAdsComponent.BROAD_MATCH_ADS
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.product.broadmatch.BroadMatchItemDataView
import com.tokopedia.search.result.product.broadmatch.BroadMatchDataView
import com.tokopedia.search.result.product.broadmatch.DynamicCarouselProduct
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val broadMatchResponseCode0Page1Position1 = "searchproduct/broadmatch/response-code-0-page-1-position-1.json"
private const val dynamicProductCarousel = "searchproduct/inspirationcarousel/dynamic-product.json"

internal class SearchProductHandleBroadMatchImpression: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Impressed top ads broad match`() {
        val searchProductModel = broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel)

        val broadMatchAds = findBroadMatchItemFromVisitableList(true)
        `When broad match product impressed`(broadMatchAds)

        `Then verify broad match top ads impressed`(broadMatchAds)
        `Then verify interaction for Broad Match Item impression`(broadMatchAds)
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

    private fun `When broad match product impressed`(broadMatchAdsData: BroadMatchItemDataView) {
        productListPresenter.onBroadMatchItemImpressed(broadMatchAdsData)
    }

    private fun `Then verify broad match top ads impressed`(broadMatchAdsData: BroadMatchItemDataView) {
        verify {
            topAdsUrlHitter.hitImpressionUrl(
                className,
                broadMatchAdsData.topAdsViewUrl,
                broadMatchAdsData.id,
                broadMatchAdsData.name,
                broadMatchAdsData.imageUrl,
                BROAD_MATCH_ADS
            )
        }
    }

    private fun `Then verify interaction for Broad Match Item impression`(itemData: BroadMatchItemDataView) {
        verify {
            broadMatchView.trackEventImpressionBroadMatchItem(itemData)
        }

        verify(exactly = 0) {
            inspirationCarouselDynamicProductView.trackDynamicProductCarouselImpression(
                any(),
                any(),
                any()
            )
        }
    }

    @Test
    fun `Impressed non top ads broad match`() {
        val searchProductModel = broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel)

        val broadMatchNotAds = findBroadMatchItemFromVisitableList(false)
        `When broad match product impressed`(broadMatchNotAds)

        `Then verify interaction for Broad Match Item impression`(broadMatchNotAds)
        confirmVerified(topAdsUrlHitter)
    }

    @Test
    fun `Impressed inspiration dynamic carousel broad match`() {
        val searchProductModel = dynamicProductCarousel.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel)

        val dynamicProductCarousel = findBroadMatchItemFromVisitableList(false)
        `When broad match product impressed`(dynamicProductCarousel)

        `Then verify interaction for dynamic carousel impression`(dynamicProductCarousel)
    }

    private fun `Then verify interaction for dynamic carousel impression`(dynamicProductCarousel: BroadMatchItemDataView) {
        verify {
            val carouselProductType = dynamicProductCarousel.carouselProductType as DynamicCarouselProduct
            inspirationCarouselDynamicProductView.trackDynamicProductCarouselImpression(
                dynamicProductCarousel,
                carouselProductType.type,
                carouselProductType.inspirationCarouselProduct,
            )
        }

        verify(exactly = 0) {
            broadMatchView.trackEventImpressionBroadMatchItem(any())
        }
    }

    @Test
    fun `impression broad match`() {
        val searchProductModel = broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel)

        val broadMatchDataView = findBroadMatchDataViewFromVisitableList()
        `When broad match impressed`(broadMatchDataView)

        `Then verify impression broad match`(broadMatchDataView)
    }

    private fun findBroadMatchDataViewFromVisitableList(): BroadMatchDataView {
        val visitableList = visitableListSlot.captured

        return visitableList.find { it is BroadMatchDataView } as BroadMatchDataView
    }

    private fun `When broad match impressed`(broadMatchDataView: BroadMatchDataView) {
        productListPresenter.onBroadMatchImpressed(broadMatchDataView)
    }

    private fun `Then verify impression broad match`(broadMatchDataView: BroadMatchDataView) {
        verify {
            broadMatchView.trackEventImpressionBroadMatch(broadMatchDataView)
        }
    }

    @Test
    fun `impression dynamic carousel as broad match`() {
        val searchProductModel = dynamicProductCarousel.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel)

        val broadMatchDataView = findBroadMatchDataViewFromVisitableList()
        `When broad match impressed`(broadMatchDataView)

        `Then verify broad match impression is not called`()
    }

    private fun `Then verify broad match impression is not called`() {
        verify(exactly = 0) {
            broadMatchView.trackEventImpressionBroadMatch(any())
        }
    }
}
