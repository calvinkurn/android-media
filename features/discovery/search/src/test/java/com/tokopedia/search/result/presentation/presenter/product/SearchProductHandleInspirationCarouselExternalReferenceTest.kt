package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

private const val inFirstPage = "searchproduct/inspirationcarousel/in-first-page.json"
private const val chips = "searchproduct/inspirationcarousel/chips.json"
private const val chipProducts1 =
    "searchproduct/inspirationcarousel/chipproducts/chip-products-1.json"

internal class SearchProductHandleInspirationCarouselExternalReferenceTest :
    ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList: List<Visitable<*>> by lazy { visitableListSlot.captured }
    private val className = "SearchClassName"
    private var externalReference : String = ""

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
        every { productListView.className } returns className
    }

    private fun `Given view already load data`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        productListPresenter.loadData(mapOf(
            SearchApiConst.SRP_EXT_REF to externalReference
        ))
    }

    private fun `When inspiration carousel product impressed`(product: InspirationCarouselDataView.Option.Product) {
        productListPresenter.onInspirationCarouselProductImpressed(product)
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

    @Test
    fun `Inspiration carousel product with externalReference`() {
        externalReference = "1234567"
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST,
            false
        )
        `When inspiration carousel product impressed`(inspirationCarouselProduct)

        `Then verify externalReference`(inspirationCarouselProduct.externalReference)
    }

    @Test
    fun `Inspiration carousel product without externalReference`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST,
            false
        )
        `When inspiration carousel product impressed`(inspirationCarouselProduct)

        `Then verify externalReference`(inspirationCarouselProduct.externalReference)
    }


    @Test
    fun `Top ads inspiration carousel chips product with externalReference`() {
        externalReference = "1234567"
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val selectedOption = `Given carousel chips clicked will return selected chip option`()
        val inspirationCarouselProduct = findProductFromInspirationCarouselDataViewOption(
            selectedOption,
            true
        )

        `Then verify externalReference`(inspirationCarouselProduct.externalReference)
    }

    @Test
    fun `Top ads inspiration carousel chips product without externalReference`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val selectedOption = `Given carousel chips clicked will return selected chip option`()
        val inspirationCarouselProduct = findProductFromInspirationCarouselDataViewOption(
            selectedOption,
            true
        )

        `Then verify externalReference`(inspirationCarouselProduct.externalReference)
    }

    @Test
    fun `Inspiration carousel chips product with externalReference`() {
        externalReference = "1234567"
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val selectedOption = `Given carousel chips clicked will return selected chip option`()

        val inspirationCarouselProduct = findProductFromInspirationCarouselDataViewOption(
            selectedOption,
            false
        )

        `When inspiration carousel product impressed`(inspirationCarouselProduct)

        `Then verify externalReference`(inspirationCarouselProduct.externalReference)
    }
    
    @Test
    fun `Inspiration carousel chips product without externalReference`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given View already load data with inspiration carousel`(searchProductModel)

        val selectedOption = `Given carousel chips clicked will return selected chip option`()

        val inspirationCarouselProduct = findProductFromInspirationCarouselDataViewOption(
            selectedOption,
            false
        )

        `When inspiration carousel product impressed`(inspirationCarouselProduct)

        `Then verify externalReference`(inspirationCarouselProduct.externalReference)
    }

    private fun `Then verify externalReference`(actualExternalReference: String) {
        actualExternalReference shouldBe externalReference
    }
}