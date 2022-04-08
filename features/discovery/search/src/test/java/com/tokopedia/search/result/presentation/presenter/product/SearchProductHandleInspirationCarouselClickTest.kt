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
private const val chips = "searchproduct/inspirationcarousel/chips.json"

internal class SearchProductHandleInspirationCarouselClickTest :
    ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val className = "SearchClassName"

    @Test
    fun `Click top ads inspiration carousel list`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList("list",true)
        `When broad match product clicked`(inspirationCarouselProduct)

        `Then verify inspiration carousel product top ads clicked`(inspirationCarouselProduct)
        `Then verify interaction for Inspiration Carousel Product List click`(
            inspirationCarouselProduct
        )
    }

    @Test
    fun `Click non top ads inspiration carousel list`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList("list",false)
        `When broad match product clicked`(inspirationCarouselProduct)

        `Then verify interaction for Inspiration Carousel Product List click`(
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

    private fun `When broad match product clicked`(product: InspirationCarouselDataView.Option.Product) {
        productListPresenter.onInspirationCarouselProductClick(product)
    }

    private fun `Then verify inspiration carousel product top ads clicked`(product: InspirationCarouselDataView.Option.Product) {
        verify {
            productListView.className

            topAdsUrlHitter.hitClickUrl(
                className,
                product.topAdsClickUrl,
                product.id,
                product.name,
                product.imgUrl,
                ORGANIC_ADS
            )
        }
    }

    private fun `Then verify interaction for Inspiration Carousel Product List click`(product: InspirationCarouselDataView.Option.Product) {
        verify {
            productListView.redirectionStartActivity(product.applink, product.url)
            productListView.trackEventClickInspirationCarouselListItem(product)
        }

        verify(exactly = 0) {
            productListView.trackEventClickInspirationCarouselGridItem(product)
            productListView.trackEventClickInspirationCarouselChipsItem(product)
        }
    }

    private fun `Then verify interaction for Inspiration Carousel Product Grid click`(product: InspirationCarouselDataView.Option.Product) {
        verify {
            productListView.redirectionStartActivity(product.applink, product.url)
            productListView.trackEventClickInspirationCarouselGridItem(product)
        }

        verify(exactly = 0) {
            productListView.trackEventClickInspirationCarouselListItem(product)
            productListView.trackEventClickInspirationCarouselChipsItem(product)
        }
    }

    private fun `Then verify interaction for Inspiration Carousel Product Chips Item click`(
        product: InspirationCarouselDataView.Option.Product
    ) {
        verify {
            productListView.redirectionStartActivity(product.applink, product.url)
            productListView.trackEventClickInspirationCarouselChipsItem(product)
        }

        verify(exactly = 0) {
            productListView.trackEventClickInspirationCarouselListItem(product)
            productListView.trackEventClickInspirationCarouselGridItem(product)
        }
    }

    @Test
    fun `Click top ads inspiration carousel grid`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList("grid",true)
        `When broad match product clicked`(inspirationCarouselProduct)

        `Then verify inspiration carousel product top ads clicked`(inspirationCarouselProduct)
        `Then verify interaction for Inspiration Carousel Product Grid click`(
            inspirationCarouselProduct
        )
    }

    @Test
    fun `Click non top ads inspiration carousel grid`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList("grid",false)
        `When broad match product clicked`(inspirationCarouselProduct)

        `Then verify interaction for Inspiration Carousel Product Grid click`(
            inspirationCarouselProduct
        )
        confirmVerified(topAdsUrlHitter)
    }


    @Test
    fun `Click top ads inspiration carousel chips`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList("chips",true)
        `When broad match product clicked`(inspirationCarouselProduct)

        `Then verify inspiration carousel product top ads clicked`(inspirationCarouselProduct)
        `Then verify interaction for Inspiration Carousel Product Chips Item click`(
            inspirationCarouselProduct
        )
    }

    @Test
    fun `Click non top ads inspiration carousel chips`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList("chips",false)
        `When broad match product clicked`(inspirationCarouselProduct)

        `Then verify interaction for Inspiration Carousel Product Chips Item click`(
            inspirationCarouselProduct
        )
        confirmVerified(topAdsUrlHitter)
    }

}