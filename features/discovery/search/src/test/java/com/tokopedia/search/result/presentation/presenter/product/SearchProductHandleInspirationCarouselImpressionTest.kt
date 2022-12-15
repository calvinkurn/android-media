package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.TopAdsComponent.ORGANIC_ADS
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS
import com.tokopedia.search.result.product.inspirationcarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID
import com.tokopedia.search.result.product.inspirationcarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST
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
private const val chipProducts1 =
    "searchproduct/inspirationcarousel/chipproducts/chip-products-1.json"

internal class SearchProductHandleInspirationCarouselImpressionTest :
    ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList: List<Visitable<*>> by lazy { visitableListSlot.captured }

    @Test
    fun `Impressed top ads inspiration carousel list`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            LAYOUT_INSPIRATION_CAROUSEL_LIST,
            true
        )
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

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            LAYOUT_INSPIRATION_CAROUSEL_LIST,
            false
        )
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

        val inspirationCarouselLayoutList =
            visitableList.filter {
                it is InspirationCarouselDataView && it.layout == layoutType
            } as List<InspirationCarouselDataView>

        return inspirationCarouselLayoutList.asSequence().map { it.options }
            .flatten()
            .map { it.product }
            .flatten()
            .find { it.isOrganicAds == isTopAds }!!
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

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            LAYOUT_INSPIRATION_CAROUSEL_GRID,
            true
        )
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

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            LAYOUT_INSPIRATION_CAROUSEL_GRID,
            false
        )
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

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            LAYOUT_INSPIRATION_CAROUSEL_CHIPS,
            true
        )
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

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            LAYOUT_INSPIRATION_CAROUSEL_CHIPS,
            false
        )
        `When inspiration carousel product impressed`(inspirationCarouselProduct)

        `Then verify interaction for Inspiration Carousel Product Chips Item impression`(
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

        `When inspiration carousel product impressed`(inspirationCarouselProduct)

        `Then verify inspiration carousel product top ads impressed`(inspirationCarouselProduct)
        `Then verify interaction for Inspiration Carousel Product Chips Item impression`(
            inspirationCarouselProduct
        )
    }

    private fun findProductFromInspirationCarouselDataViewOption(
        option: InspirationCarouselDataView.Option,
        isTopAds: Boolean
    ): InspirationCarouselDataView.Option.Product {
        return option.product.find { it.isOrganicAds == isTopAds }!!
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

        `When inspiration carousel product impressed`(inspirationCarouselProduct)

        `Then verify interaction for Inspiration Carousel Product Chips Item impression`(
            inspirationCarouselProduct
        )
        confirmVerified(topAdsUrlHitter)
    }


    private fun List<Visitable<*>>.findIndexedChipsCarousel(): IndexedValue<InspirationCarouselDataView> {
        val indexedVisitable = withIndex().find {
            it.value is InspirationCarouselDataView
                    && (it.value as InspirationCarouselDataView).layout == LAYOUT_INSPIRATION_CAROUSEL_CHIPS
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
