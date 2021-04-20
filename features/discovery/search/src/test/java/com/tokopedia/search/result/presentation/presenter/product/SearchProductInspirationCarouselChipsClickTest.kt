package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val chips = "searchproduct/inspirationcarousel/chips.json"
private const val chipProducts1 = "searchproduct/inspirationcarousel/chipproducts/chip-products-1.json"

internal class SearchProductInspirationCarouselChipsClickTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

    @Test
    fun `Click inspiration carousel chips without products should call API`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given view already load data`(searchProductModel)

        val inspirationCarouselDataViewIndexed = visitableList.findIndexedChipsCarousel()
        val adapterPosition = inspirationCarouselDataViewIndexed.index
        val clickedInspirationCarouselOption = inspirationCarouselDataViewIndexed.value.options[1]

        `When inspiration carousel chips clicked`(
                adapterPosition,
                inspirationCarouselDataViewIndexed.value,
                clickedInspirationCarouselOption
        )

        `Then verify tracking chips click`(clickedInspirationCarouselOption)
        `Then assert view is refreshed`(adapterPosition)
        `Then assert get inspiration carousel chips API is called`(clickedInspirationCarouselOption.identifier)
    }

    private fun `Given view already load data`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        productListPresenter.loadData(mapOf(SearchApiConst.Q to "samsung"))
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

    private fun `When inspiration carousel chips clicked`(
            adapterPosition: Int,
            inspirationCarouselDataView: InspirationCarouselDataView,
            clickedInspirationCarouselOption: InspirationCarouselDataView.Option
    ) {
        productListPresenter.onInspirationCarouselChipsClick(
                adapterPosition,
                inspirationCarouselDataView,
                clickedInspirationCarouselOption,
                mapOf()
        )
    }

    private fun `Then assert get inspiration carousel chips API is called`(expectedIdentifier: String) {
        val requestParamsSlot = slot<RequestParams>()

        verify {
            getInspirationCarouselChipsProductsUseCase.unsubscribe()
            getInspirationCarouselChipsProductsUseCase.execute(capture(requestParamsSlot), any())
        }

        val requestParams = requestParamsSlot.captured
        requestParams.parameters["identifier"] shouldBe expectedIdentifier
    }

    private fun `Then verify tracking chips click`(clickedInspirationCarouselOption: InspirationCarouselDataView.Option) {
        verify {
            productListView.trackInspirationCarouselChipsClicked(clickedInspirationCarouselOption)
        }
    }

    private fun `Then assert view is refreshed`(adapterPosition: Int) {
        verify {
            productListView.refreshItemAtIndex(adapterPosition)
        }
    }

    @Test
    fun `Click inspiration carousel chips with products should not call API`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given view already load data`(searchProductModel)

        val inspirationCarouselDataViewIndexed = visitableList.findIndexedChipsCarousel()
        val adapterPosition = inspirationCarouselDataViewIndexed.index
        val clickedInspirationCarouselOption = inspirationCarouselDataViewIndexed.value.options[0]

        `When inspiration carousel chips clicked`(
                adapterPosition,
                inspirationCarouselDataViewIndexed.value,
                clickedInspirationCarouselOption
        )

        `Then verify tracking chips click`(clickedInspirationCarouselOption)
        `Then assert view is refreshed`(adapterPosition)
        `Then assert get inspiration carousel chips API is not called`()
    }

    private fun `Then assert get inspiration carousel chips API is not called`() {
        verify(exactly = 0) {
            getInspirationCarouselChipsProductsUseCase.unsubscribe()
            getInspirationCarouselChipsProductsUseCase.execute(any(), any())
        }
    }

    @Test
    fun `Click inspiration carousel chips and call API success`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given view already load data`(searchProductModel)

        val chipsProductsModel = chipProducts1.jsonToObject<InspirationCarouselChipsProductModel>()
        `Given get chips product list success `(chipsProductsModel)

        val inspirationCarouselDataViewIndexed = visitableList.findIndexedChipsCarousel()
        val adapterPosition = inspirationCarouselDataViewIndexed.index
        val inspirationCarouselDataView = inspirationCarouselDataViewIndexed.value

        val clickedPosition = 1
        val clickedInspirationCarouselOption = inspirationCarouselDataView.options[clickedPosition]

        `When inspiration carousel chips clicked`(
                adapterPosition,
                inspirationCarouselDataView,
                clickedInspirationCarouselOption
        )

        `Then verify tracking chips click`(clickedInspirationCarouselOption)
        `Then verify inspiration carousel option product is updated`(
                clickedInspirationCarouselOption,
                chipsProductsModel,
                clickedPosition + 1,
        )

        `Then verify refresh view is called twice`(adapterPosition)

        inspirationCarouselDataView.options.forEachIndexed { index, option ->
            option.isChipsActive shouldBe (index == clickedPosition)
        }
    }

    private fun `Given get chips product list success `(chipsProductsModel: InspirationCarouselChipsProductModel) {
        every {
            getInspirationCarouselChipsProductsUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<InspirationCarouselChipsProductModel>>().complete(chipsProductsModel)
        }
    }

    private fun `Then verify inspiration carousel option product is updated`(
            clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
            chipsProductsModel:
            InspirationCarouselChipsProductModel,
            position: Int
    ) {
        clickedInspirationCarouselOption.product.assert(
                chipsProductsModel.searchProductCarouselByIdentifier.product,
                clickedInspirationCarouselOption.inspirationCarouselType,
                clickedInspirationCarouselOption.layout,
                position,
                clickedInspirationCarouselOption.title,
        )
    }

    private fun `Then verify refresh view is called twice`(adapterPosition: Int) {
        verify(exactly = 2) {
            productListView.refreshItemAtIndex(adapterPosition)
        }
    }
}