package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

private const val searchProductWithTopAdsResponseJSON = "searchproduct/with-topads.json"
private const val broadMatchResponseCode0Page1Position1 =
    "searchproduct/broadmatch/response-code-0-page-1-position-1.json"
private const val dynamicProductCarousel = "searchproduct/inspirationcarousel/dynamic-product.json"
private const val inFirstPage = "searchproduct/inspirationcarousel/in-first-page.json"
private const val chips = "searchproduct/inspirationcarousel/chips.json"
private const val chipProducts1 =
    "searchproduct/inspirationcarousel/chipproducts/chip-products-1.json"

internal class SearchProductHandleExternalReferenceTest : ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList: List<Visitable<*>> by lazy { visitableListSlot.captured }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When view load data`(externalReference: String) {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        productListPresenter.loadData(
            mapOf(
                SearchApiConst.SRP_EXT_REF to externalReference
            )
        )
    }

    private fun findProductItemFromVisitableList(
        isTopAds: Boolean = false,
        isOrganicAds: Boolean = false
    ): ProductItemDataView {
        val visitableList = visitableListSlot.captured

        return visitableList.find { it is ProductItemDataView && it.isTopAds == isTopAds && it.isOrganicAds == isOrganicAds } as ProductItemDataView
    }

    private fun `Then verify externalReference`(
        actualExternalReference: String,
        expectedExternalReference: String = "",
    ) {
        actualExternalReference shouldBe expectedExternalReference
    }

    @Test
    fun `Product with external Reference`() {
        val expectedExternalReference = "1234567"

        val searchProductModel =
            searchProductWithTopAdsResponseJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`(expectedExternalReference)

        `Then verify organic product external reference`(expectedExternalReference)
        `Then verify organic ads external reference`(expectedExternalReference)
        `Then verify top ads external reference`(expectedExternalReference)
    }

    @Test
    fun `Product without external Reference`() {
        val searchProductModel =
            searchProductWithTopAdsResponseJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`("")

        `Then verify organic product external reference`()
        `Then verify organic ads external reference`()
        `Then verify top ads external reference`()

    }

    private fun `Then verify organic product external reference`(expectedExternalReference: String = "") {
        val productItemViewModel =
            findProductItemFromVisitableList(isTopAds = false, isOrganicAds = false)

        `Then verify externalReference`(
            productItemViewModel.dimension131,
            expectedExternalReference
        )
    }

    private fun `Then verify organic ads external reference`(expectedExternalReference: String = "") {
        val organicAdsViewModel =
            findProductItemFromVisitableList(isTopAds = false, isOrganicAds = true)

        `Then verify externalReference`(organicAdsViewModel.dimension131, expectedExternalReference)
    }

    private fun `Then verify top ads external reference`(expectedExternalReference: String = "") {
        val topAdsViewModel =
            findProductItemFromVisitableList(isTopAds = true, isOrganicAds = false)

        `Then verify externalReference`(topAdsViewModel.dimension131, expectedExternalReference)
    }

    @Test
    fun `Top ads broad match item with externalReference`() {
        val expectedExternalReference = "1234567"
        val searchProductModel =
            broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`(expectedExternalReference)

        `Then verify broad match ads external reference`(expectedExternalReference)
    }

    @Test
    fun `Top ads broad match item without externalReference`() {
        val searchProductModel =
            broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`("")

        `Then verify broad match ads external reference`()
    }

    @Test
    fun `Broad match item with externalReference`() {
        val expectedExternalReference = "1234567"
        val searchProductModel =
            broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`(expectedExternalReference)

        `Then verify broad match non ads external reference`(expectedExternalReference)
    }

    @Test
    fun `Broad match item without externalReference`() {
        val searchProductModel =
            broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`("")

        `Then verify broad match non ads external reference`()
    }

    @Test
    fun `Dynamic carousel as broad match with externalReference`() {
        val expectedExternalReference = "1234567"
        val searchProductModel = dynamicProductCarousel.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`(expectedExternalReference)

        `Then verify broad match non ads external reference`(expectedExternalReference)
    }

    @Test
    fun `Dynamic carousel as broad match without externalReference`() {
        val searchProductModel = dynamicProductCarousel.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`("")

        `Then verify broad match non ads external reference`()
    }

    private fun findBroadMatchItemFromVisitableList(isTopAds: Boolean): BroadMatchItemDataView {
        val visitableList = visitableListSlot.captured

        val broadMatchViewModel =
            visitableList.find { it is BroadMatchDataView } as BroadMatchDataView

        return broadMatchViewModel.broadMatchItemDataViewList.find { it.isOrganicAds == isTopAds }!!
    }

    private fun `Then verify broad match ads external reference`(
        expectedExternalReference: String = "",
    ) {
        val broadMatchAds = findBroadMatchItemFromVisitableList(true)

        `Then verify externalReference`(broadMatchAds.externalReference, expectedExternalReference)
    }

    private fun `Then verify broad match non ads external reference`(
        expectedExternalReference: String = "",
    ) {
        val broadMatch = findBroadMatchItemFromVisitableList(false)

        `Then verify externalReference`(broadMatch.externalReference, expectedExternalReference)
    }

    @Test
    fun `Inspiration carousel product with externalReference`() {
        val expectedExternalReference = "1234567"
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`(expectedExternalReference)

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST,
            false
        )

        `Then verify externalReference`(
            inspirationCarouselProduct.externalReference,
            expectedExternalReference
        )
    }

    @Test
    fun `Inspiration carousel product without externalReference`() {
        val searchProductModel = inFirstPage.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`("")

        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST,
            false
        )

        `Then verify externalReference`(inspirationCarouselProduct.externalReference)
    }

    @Test
    fun `Top ads inspiration carousel chips product with externalReference`() {
        val expectedExternalReference = "1234567"
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`(expectedExternalReference)

        val selectedOption = `Given carousel chips clicked will return selected chip option`()
        val inspirationCarouselProduct = findProductFromInspirationCarouselDataViewOption(
            selectedOption,
            true
        )

        `Then verify externalReference`(
            inspirationCarouselProduct.externalReference,
            expectedExternalReference
        )
    }

    @Test
    fun `Top ads inspiration carousel chips product without externalReference`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`("")

        val selectedOption = `Given carousel chips clicked will return selected chip option`()

        val inspirationCarouselProduct = findProductFromInspirationCarouselDataViewOption(
            selectedOption,
            true
        )

        `Then verify externalReference`(inspirationCarouselProduct.externalReference)
    }

    @Test
    fun `Inspiration carousel chips product with externalReference`() {
        val expectedExternalReference = "1234567"
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`(expectedExternalReference)

        val selectedOption = `Given carousel chips clicked will return selected chip option`()

        val inspirationCarouselProduct = findProductFromInspirationCarouselDataViewOption(
            selectedOption,
            false
        )

        `Then verify externalReference`(
            inspirationCarouselProduct.externalReference,
            expectedExternalReference
        )
    }

    @Test
    fun `Inspiration carousel chips product without externalReference`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`("")

        val selectedOption = `Given carousel chips clicked will return selected chip option`()

        val inspirationCarouselProduct = findProductFromInspirationCarouselDataViewOption(
            selectedOption,
            false
        )

        `Then verify externalReference`(inspirationCarouselProduct.externalReference)
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