package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.TopAdsComponent.ORGANIC_ADS
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val inFirstPage = "searchproduct/inspirationcarousel/in-first-page.json"
private const val inFirstPageNoTopads = "searchproduct/inspirationcarousel/in-first-page-no-topads.json"
private const val chips = "searchproduct/inspirationcarousel/chips.json"
private const val dynamicProduct = "searchproduct/inspirationcarousel/dynamic-product.json"
private const val dynamicProductNoSuggestion = "searchproduct/inspirationcarousel/dynamic-product-no-suggestion.json"
private const val keywordProduct = "searchproduct/inspirationcarousel/keyword-product.json"

internal class SearchProductHandleInspirationCarouselImpressionTest :
    ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val className = "SearchClassName"

    @Test
    fun `Impressed top ads inspiration carousel list`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList("list",true)
        `When inspiration carousel product impressed`(inspirationCarouselProduct)

        `Then verify inspiration carousel product top ads impressed`(inspirationCarouselProduct)
        `Then verify interaction for Inspiration Carousel Product List impression`(
            inspirationCarouselProduct
        )
    }

    @Test
    fun `Impressed non top ads inspiration carousel list`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList("list",false)
        `When inspiration carousel product impressed`(inspirationCarouselProduct)

        `Then verify interaction for Inspiration Carousel Product List impression`(
            inspirationCarouselProduct
        )
        confirmVerified(topAdsUrlHitter)
    }

    private fun `Given View already load data with inspiration carousel`(searchProductModel: SearchProductModel) {
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given class name`()
        `Given view already load data`()
    }

    private fun findInspirationCarouselProductFromVisitableList(
        layoutType: String,
        isTopAds: Boolean
    ): InspirationCarouselDataView.Option.Product {
        val visitableList = visitableListSlot.captured

        val carousel = visitableList.find { it is InspirationCarouselDataView && it.layout == layoutType } as InspirationCarouselDataView

        val listOption = carousel.options.first { it.product.firstOrNull { it.isOrganicAds == isTopAds } != null }
        return listOption.product.find { it.isOrganicAds == isTopAds }!!
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

    private fun `When inspiration carousel product impressed`(product: InspirationCarouselDataView.Option.Product) {
        productListPresenter.onInspirationCarouselProductImpressed(product)
    }

    private fun `Then verify inspiration carousel product top ads impressed`(product: InspirationCarouselDataView.Option.Product) {
        verify {
            productListView.className

            topAdsUrlHitter.hitImpressionUrl(
                className,
                product.topAdsViewUrl,
                product.id,
                product.name,
                product.imgUrl,
                ORGANIC_ADS
            )
        }
    }

    private fun `Then verify interaction for Inspiration Carousel Product List impression`(product: InspirationCarouselDataView.Option.Product) {
        verify {
            productListView.trackEventImpressionInspirationCarouselListItem(product)
        }

        verify(exactly = 0) {
            productListView.trackEventImpressionInspirationCarouselGridItem(product)
            productListView.trackEventImpressionInspirationCarouselChipsItem(product)
        }
    }

    private fun `Then verify interaction for Inspiration Carousel Product Grid impression`(product: InspirationCarouselDataView.Option.Product) {
        verify {
            productListView.trackEventImpressionInspirationCarouselGridItem(product)
        }

        verify(exactly = 0) {
            productListView.trackEventImpressionInspirationCarouselListItem(product)
            productListView.trackEventImpressionInspirationCarouselChipsItem(product)
        }
    }

    private fun `Then verify interaction for Inspiration Carousel Product Chips Item impression`(
        product: InspirationCarouselDataView.Option.Product
    ) {
        verify {
            productListView.trackEventImpressionInspirationCarouselChipsItem(product)
        }

        verify(exactly = 0) {
            productListView.trackEventImpressionInspirationCarouselListItem(product)
            productListView.trackEventImpressionInspirationCarouselGridItem(product)
        }
    }

    @Test
    fun `Impressed top ads inspiration carousel grid`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList("grid",true)
        `When inspiration carousel product impressed`(inspirationCarouselProduct)

        `Then verify inspiration carousel product top ads impressed`(inspirationCarouselProduct)
        `Then verify interaction for Inspiration Carousel Product Grid impression`(
            inspirationCarouselProduct
        )
    }

    @Test
    fun `Impressed non top ads inspiration carousel grid`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList("grid",false)
        `When inspiration carousel product impressed`(inspirationCarouselProduct)

        `Then verify interaction for Inspiration Carousel Product Grid impression`(
            inspirationCarouselProduct
        )
        confirmVerified(topAdsUrlHitter)
    }


    @Test
    fun `Impressed top ads inspiration carousel chips`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList("chips",true)
        `When inspiration carousel product impressed`(inspirationCarouselProduct)

        `Then verify inspiration carousel product top ads impressed`(inspirationCarouselProduct)
        `Then verify interaction for Inspiration Carousel Product Chips Item impression`(
            inspirationCarouselProduct
        )
    }

    @Test
    fun `Impressed non top ads inspiration carousel chips`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList("chips",false)
        `When inspiration carousel product impressed`(inspirationCarouselProduct)

        `Then verify interaction for Inspiration Carousel Product Chips Item impression`(
            inspirationCarouselProduct
        )
        confirmVerified(topAdsUrlHitter)
    }

}