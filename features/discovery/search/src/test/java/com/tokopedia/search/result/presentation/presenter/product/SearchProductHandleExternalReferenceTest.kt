package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.product.broadmatch.BroadMatchDataView
import com.tokopedia.search.result.product.broadmatch.BroadMatchItemDataView
import com.tokopedia.search.result.product.broadmatch.DynamicCarouselOption
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS
import com.tokopedia.search.result.product.inspirationcarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

private const val externalReferenceResponseJSON = "searchproduct/externalreference/externalreference.json"
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

        val searchProductModel = externalReferenceResponseJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`(expectedExternalReference)

        `Then verify organic product external reference`(expectedExternalReference)
        `Then verify organic ads external reference`(expectedExternalReference)
        `Then verify top ads external reference`(expectedExternalReference)

        `Then verify broad match ads external reference`(expectedExternalReference)
        `Then verify broad match non ads external reference`(expectedExternalReference)
        `Then verify dynamic product carousel external reference`(expectedExternalReference)

        `Then verify inspiration carousel product external reference`(expectedExternalReference)
        `Then verify top ads carousel chips product external reference`(expectedExternalReference)
        `Then verify carousel chips product external reference`(expectedExternalReference)
    }

    @Test
    fun `Product without external Reference`() {
        val searchProductModel = externalReferenceResponseJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When view load data`("")

        `Then verify organic product external reference`()
        `Then verify organic ads external reference`()
        `Then verify top ads external reference`()

        `Then verify broad match ads external reference`()
        `Then verify broad match non ads external reference`()
        `Then verify dynamic product carousel external reference`()

        `Then verify inspiration carousel product external reference`()
        `Then verify top ads carousel chips product external reference`()
        `Then verify carousel chips product external reference`()

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

    private fun findBroadMatchItemFromVisitableList(isTopAds: Boolean): BroadMatchItemDataView {
        val visitableList = visitableListSlot.captured

        val broadMatchViewModel =
            visitableList.find { it is BroadMatchDataView } as BroadMatchDataView

        return broadMatchViewModel.broadMatchItemDataViewList.find { it.isOrganicAds == isTopAds }!!
    }

    private fun findDynamicProductItemFromVisitableList(isTopAds: Boolean): BroadMatchItemDataView {
        val visitableList = visitableListSlot.captured

        val broadMatchViewModel =
            visitableList.find { it is BroadMatchDataView && it.carouselOptionType is DynamicCarouselOption } as BroadMatchDataView

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

    private fun `Then verify dynamic product carousel external reference`(
        expectedExternalReference: String = "",
    ) {
        val broadMatch = findDynamicProductItemFromVisitableList(false)

        `Then verify externalReference`(broadMatch.externalReference, expectedExternalReference)
    }

    private fun `Then verify inspiration carousel product external reference`(expectedExternalReference: String = "") {
        val inspirationCarouselProduct = findInspirationCarouselProductFromVisitableList(
            LAYOUT_INSPIRATION_CAROUSEL_LIST,
            false
        )

        `Then verify externalReference`(
            inspirationCarouselProduct.externalReference,
            expectedExternalReference
        )
    }

    private fun `Then verify top ads carousel chips product external reference`(
        expectedExternalReference: String =""
    ) {
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

    private fun `Then verify carousel chips product external reference`(
        expectedExternalReference: String =""
    ) {
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
