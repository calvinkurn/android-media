package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.TopAdsComponent.ORGANIC_ADS
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselContract
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselPresenter
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import rx.Subscriber

private const val inFirstPage = "searchproduct/inspirationcarousel/in-first-page.json"
private const val chips = "searchproduct/inspirationcarousel/chips.json"
private const val chipProducts1 =
    "searchproduct/inspirationcarousel/chipproducts/chip-products-1.json"

internal class SearchProductHandleInspirationCarouselClickTest :
    ProductListPresenterTestFixtures() {

    private val inspirationCarouselView = mockk<InspirationCarouselContract.View>(relaxed = true)
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList: List<Visitable<*>> by lazy { visitableListSlot.captured }
    private val className = "SearchClassName"

    private lateinit var inspirationCarouselPresenter: InspirationCarouselPresenter

    @Before
    override fun setUp() {
        super.setUp()
        inspirationCarouselPresenter = InspirationCarouselPresenter(topAdsUrlHitter)
        inspirationCarouselPresenter.attachView(inspirationCarouselView)
    }

    @Test
    fun `Click top ads inspiration carousel list`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST,
            true
        )
        `When inspiration carousel product clicked`(inspirationCarouselProduct)

        `Then verify inspiration carousel product top ads clicked`(inspirationCarouselProduct)
        `Then verify interaction for Inspiration Carousel Product List click`(
            inspirationCarouselProduct
        )
    }

    @Test
    fun `Click non top ads inspiration carousel list`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST,
            false
        )
        `When inspiration carousel product clicked`(inspirationCarouselProduct)

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

        val carousel = visitableList.find {
            it is InspirationCarouselDataView && it.layout == layoutType
        } as InspirationCarouselDataView

        val option =
            carousel.options.first { it.product.firstOrNull { it.isOrganicAds == isTopAds } != null }
        return findProductFromInspirationCarouselDataViewOption(option, isTopAds)
    }

    private fun findProductFromInspirationCarouselDataViewOption(
        option: InspirationCarouselDataView.Option,
        isTopAds: Boolean
    ): InspirationCarouselDataView.Option.Product {
        return option.product.find { it.isOrganicAds == isTopAds }!!
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given class name`() {
        every { inspirationCarouselView.className } returns className
    }

    private fun `Given view already load data`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        productListPresenter.loadData(mapOf())
    }

    private fun `When inspiration carousel product clicked`(product: InspirationCarouselDataView.Option.Product) {
        inspirationCarouselPresenter.onInspirationCarouselProductClick(product)
    }

    private fun `Then verify inspiration carousel product top ads clicked`(product: InspirationCarouselDataView.Option.Product) {
        verify {
            inspirationCarouselView.className

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
            inspirationCarouselView.redirectionStartActivity(product.applink, product.url)
            inspirationCarouselView.trackEventClickInspirationCarouselListItem(product)
        }

        verify(exactly = 0) {
            inspirationCarouselView.trackEventClickInspirationCarouselGridItem(product)
            inspirationCarouselView.trackEventClickInspirationCarouselChipsItem(product)
        }
    }

    private fun `Then verify interaction for Inspiration Carousel Product Grid click`(product: InspirationCarouselDataView.Option.Product) {
        verify {
            inspirationCarouselView.redirectionStartActivity(product.applink, product.url)
            inspirationCarouselView.trackEventClickInspirationCarouselGridItem(product)
        }

        verify(exactly = 0) {
            inspirationCarouselView.trackEventClickInspirationCarouselListItem(product)
            inspirationCarouselView.trackEventClickInspirationCarouselChipsItem(product)
        }
    }

    private fun `Then verify interaction for Inspiration Carousel Product Chips Item click`(
        product: InspirationCarouselDataView.Option.Product
    ) {
        verify {
            inspirationCarouselView.redirectionStartActivity(product.applink, product.url)
            inspirationCarouselView.trackEventClickInspirationCarouselChipsItem(product)
        }

        verify(exactly = 0) {
            inspirationCarouselView.trackEventClickInspirationCarouselListItem(product)
            inspirationCarouselView.trackEventClickInspirationCarouselGridItem(product)
        }
    }

    @Test
    fun `Click top ads inspiration carousel grid`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID,
            true
        )
        `When inspiration carousel product clicked`(inspirationCarouselProduct)

        `Then verify inspiration carousel product top ads clicked`(inspirationCarouselProduct)
        `Then verify interaction for Inspiration Carousel Product Grid click`(
            inspirationCarouselProduct
        )
    }

    @Test
    fun `Click non top ads inspiration carousel grid`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID,
            false
        )
        `When inspiration carousel product clicked`(inspirationCarouselProduct)

        `Then verify interaction for Inspiration Carousel Product Grid click`(
            inspirationCarouselProduct
        )
        confirmVerified(topAdsUrlHitter)
    }


    @Test
    fun `Click top ads inspiration carousel chips`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS,
            true
        )
        `When inspiration carousel product clicked`(inspirationCarouselProduct)

        `Then verify inspiration carousel product top ads clicked`(inspirationCarouselProduct)
        `Then verify interaction for Inspiration Carousel Product Chips Item click`(
            inspirationCarouselProduct
        )
    }

    @Test
    fun `Click non top ads inspiration carousel chips`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS,
            false
        )
        `When inspiration carousel product clicked`(inspirationCarouselProduct)

        `Then verify interaction for Inspiration Carousel Product Chips Item click`(
            inspirationCarouselProduct
        )
        confirmVerified(topAdsUrlHitter)
    }

    @Test
    fun `Click top ads inspiration carousel chips product`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val selectedOption = `Given carousel chips clicked will return selected chip option`()
        val inspirationCarouselProduct = findProductFromInspirationCarouselDataViewOption(
            selectedOption,
            true
        )

        `When inspiration carousel product clicked`(inspirationCarouselProduct)

        `Then verify inspiration carousel product top ads clicked`(inspirationCarouselProduct)
        `Then verify interaction for Inspiration Carousel Product Chips Item click`(
            inspirationCarouselProduct
        )
    }

    @Test
    fun `Click non top ads inspiration carousel chips product`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val selectedOption = `Given carousel chips clicked will return selected chip option`()

        val inspirationCarouselProduct = findProductFromInspirationCarouselDataViewOption(
            selectedOption,
            false
        )

        `When inspiration carousel product clicked`(inspirationCarouselProduct)

        `Then verify interaction for Inspiration Carousel Product Chips Item click`(
            inspirationCarouselProduct
        )
        confirmVerified(topAdsUrlHitter)
    }


    private fun List<Visitable<*>>.findIndexedChipsCarousel(): IndexedValue<InspirationCarouselDataView> {
        val indexedVisitable = withIndex().find {
            it.value is InspirationCarouselDataView
                    && (it.value as InspirationCarouselDataView).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS
        }!!

        return IndexedValue(
            index = indexedVisitable.index,
            value = indexedVisitable.value as InspirationCarouselDataView
        )
    }

    private fun `Given carousel chips clicked will return selected chip option`(): InspirationCarouselDataView.Option {
        val inspirationCarouselDataViewIndexed = visitableList.findIndexedChipsCarousel()
        val adapterPosition = inspirationCarouselDataViewIndexed.index

        val selectedOptionPosition = 1
        val clickedInspirationCarouselOption =
            inspirationCarouselDataViewIndexed.value.options[selectedOptionPosition]

        val chipsProductsModel = chipProducts1.jsonToObject<InspirationCarouselChipsProductModel>()
        `Given get chips product list success `(chipsProductsModel)

        productListPresenter.onInspirationCarouselChipsClick(
            adapterPosition,
            inspirationCarouselDataViewIndexed.value,
            clickedInspirationCarouselOption,
            mapOf()
        )

        return clickedInspirationCarouselOption
    }


    private fun `Given get chips product list success `(chipsProductsModel: InspirationCarouselChipsProductModel) {
        every {
            getInspirationCarouselChipsProductsUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<InspirationCarouselChipsProductModel>>().complete(
                chipsProductsModel
            )
        }
    }

}