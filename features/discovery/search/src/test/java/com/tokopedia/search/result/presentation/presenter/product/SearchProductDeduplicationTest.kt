package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.AceSearchProductModelV5
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductV5
import com.tokopedia.search.result.product.broadmatch.BroadMatchDataView
import com.tokopedia.search.result.product.deduplication.Deduplication.Companion.MINIMUM_CAROUSEL_PRODUCT_COUNT
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductItemDataView
import com.tokopedia.search.result.product.suggestion.SuggestionDataView
import com.tokopedia.topads.sdk.domain.model.Product
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import rx.Subscriber

internal class SearchProductDeduplicationTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

    @Test
    fun `remove products in product_list carousel with same id as product organic, product ads, and product shop ads`() {
        val searchProductModel = "searchproduct/deduplication/carousel/product_list.json"
            .jsonToObject<SearchProductModel>()

        `Given search product API will be successful`(searchProductModel)
        `Given visitable list will be captured`()

        `When load data`()

        val carouselProductIdList = visitableList
            .filterIsInstance<BroadMatchDataView>()
            .flatMap { it.broadMatchItemDataViewList }
            .map { it.id }

        val allExistingProductIdList = getAllExistingProductIdList(searchProductModel)

        assertFalse(carouselProductIdList.any(allExistingProductIdList::contains))
    }

    private fun `Given search product API will be successful`(searchProductModel: SearchProductModel) {
        every {
            searchProductFirstPageUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given visitable list will be captured`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs
    }

    private fun `When load data`() {
        productListPresenter.loadData(mapOf(
            SearchApiConst.SRP_EXT_REF to dummyExternalReference
        ))
    }

    private fun getAllExistingProductIdList(searchProductModel: SearchProductModel): List<String> {
        val shopAdsProductIdList = searchProductModel
            .cpmModel
            .data
            .flatMap { it.cpm.cpmShop.products }
            .map(Product::id)

        val organicProductIdList = searchProductModel
            .searchProduct
            .data
            .productList
            .map(SearchProductModel.Product::id)

        val organicV5ProductIdList = searchProductModel
            .searchProductV5
            .data
            .productList
            .map(SearchProductV5.Data.Product::id)

        val topAdsProductIdList = searchProductModel
            .topAdsModel
            .data
            .map { it.product.id }

        return shopAdsProductIdList + organicProductIdList + topAdsProductIdList
    }

    @Test
    fun `remove product_list carousel if products is less than threshold`() {
        val searchProductModel = "searchproduct/deduplication/carousel/product_list-less-than-threshold.json"
            .jsonToObject<SearchProductModel>()

        `Given search product API will be successful`(searchProductModel)
        `Given visitable list will be captured`()

        `When load data`()

        val carouselProductIdList =
            visitableList.filterIsInstance<SuggestionDataView>() +
                visitableList.filterIsInstance<BroadMatchDataView>()

        assertTrue(carouselProductIdList.isEmpty())

        `Then verify tracker for removed option`(searchProductModel)
    }

    private fun `Then verify tracker for removed option`(searchProductModel: SearchProductModel) {
        val removedOption = searchProductModel
            .searchInspirationCarousel
            .data.first()
            .inspirationCarouselOptions.first()

        verify {
            deduplicationView.trackRemoved(
                removedOption.componentId,
                removedOption.applink,
                dummyExternalReference,
            )
        }
    }

    @Test
    fun `remove products in chips carousel with same id as product organic, product ads, and product shop ads`() {
        val searchProductModel = "searchproduct/deduplication/carousel/chips.json"
            .jsonToObject<SearchProductModel>()

        `Given search product API will be successful`(searchProductModel)
        `Given visitable list will be captured`()

        `When load data`()

        val carouselProductIdList = visitableList
            .filterIsInstance<InspirationCarouselDataView>()
            .filter { it.layout == LAYOUT_INSPIRATION_CAROUSEL_CHIPS }
            .flatMap { it.options.flatMap { option -> option.product } }
            .map { it.id }

        val allExistingProductIdList = getAllExistingProductIdList(searchProductModel)

        assertFalse(carouselProductIdList.any(allExistingProductIdList::contains))
    }

    @Test
    fun `remove chips carousel if deduplication for first option is below threshold`() {
        val searchProductModel = "searchproduct/deduplication/carousel/chips-less-than-threshold.json"
            .jsonToObject<SearchProductModel>()

        `Given search product API will be successful`(searchProductModel)
        `Given visitable list will be captured`()

        `When load data`()

        assertFalse(
            visitableList
                .filterIsInstance<InspirationCarouselDataView>()
                .any { it.layout == LAYOUT_INSPIRATION_CAROUSEL_CHIPS }
        )

        `Then verify tracker for removed option`(searchProductModel)
    }

    @Test
    fun `remove products in carousel_seamless carousel with same id as product organic, product ads, and product shop ads`() {
        val searchProductModel = "searchproduct/deduplication/carousel/carousel_seamless.json"
            .jsonToObject<SearchProductModel>()

        `Given search product API will be successful`(searchProductModel)
        `Given visitable list will be captured`()

        `When load data`()

        val carouselProductIdList = visitableList
            .filterIsInstance<InspirationProductItemDataView>()
            .map(InspirationProductItemDataView::id)

        val allExistingProductIdList = getAllExistingProductIdList(searchProductModel)

        assertFalse(carouselProductIdList.any(allExistingProductIdList::contains))
    }

    companion object {
        private const val dummyExternalReference = "1234567"
    }
}
