package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.*
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.search.shouldBe
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val inspirationCardResponseFirstPage = "searchproduct/inspirationsize/in-first-page.json"

internal class SearchProductHandleInspirationSizeTest: ProductListPresenterTestFixtures() {
    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Show inspiration card general cases`() {
        val searchProductModel = inspirationCardResponseFirstPage.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel with Inspiration Card`(searchProductModel)
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration size and product sequence on first page`(searchProductModel)

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list has correct inspiration size and product sequence after load more`(searchProductModel)
    }

    private fun `Given Search Product API will return SearchProductModel with Inspiration Card`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        every { searchProductLoadMoreUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductCommonResponseJSON.jsonToObject<SearchProductModel>())
        }
    }

    private fun `Given Mechanism to save and get product position from cache`() {
        val lastProductPositionSlot = slot<Int>()

        every { productListView.lastProductItemPositionFromCache }.answers {
            if (lastProductPositionSlot.isCaptured) lastProductPositionSlot.captured else 0
        }

        every { productListView.saveLastProductItemPositionToCache(capture(lastProductPositionSlot)) } just runs
    }

    private fun `When Load Data`() {
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }

        productListPresenter.loadData(searchParameter)

        val visitableList = visitableListSlot.captured

        `Then init size option filter and set default selected`(visitableList.filterIsInstance<InspirationSizeDataView>())
    }

    private fun `Then verify view set product list`() {
        verifyOrder {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Then verify visitable list has correct inspiration size and product sequence on first page`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured
        val inspirationWidget = searchProductModel.searchInspirationWidget.data.filter { it.type == "size_perso" }

        // 0 -> search product count data
        // 1 -> separator
        // 2 -> inspiration size (positon 0)
        // 3 -> separator
        // 4 -> product
        // 5 -> product
        // 6 -> product
        // 7 -> product
        // 8 -> product
        // 9 -> product
        // 10 -> product
        // 11 -> product
        // 12 -> product
        // 13 -> product
        // 14 -> product
        // 15 -> product
        // 16 -> product
        // 17 -> product
        // 18 -> product

        visitableList.size shouldBe 18

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                0 -> {
                    visitable.shouldBeInstanceOf<SearchProductCountDataView>(
                        "visitable list at index $index should be SearchProductCountViewModel"
                    )
                }
                1 -> {
                    visitable.shouldBeInstanceOf<SeparatorDataView>(
                        "visitable list at index $index should be Separator"
                    )
                }
                2 -> {
                    visitable.shouldBeInstanceOf<InspirationSizeDataView>(
                        "visitable list at index $index should be InspirationCardViewModel"
                    )
                    (visitable as InspirationSizeDataView).assertInspirationCardViewModel(inspirationWidget[0])
                }
                3 -> {
                    visitable.shouldBeInstanceOf<SeparatorDataView>(
                        "visitable list at index $index should be Separator"
                    )
                }
                else -> visitable.shouldBeInstanceOf<ProductItemDataView>(
                    "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    private fun InspirationSizeDataView.assertInspirationCardViewModel(inspirationWidget: SearchProductModel.InspirationCardData) {
        data.title shouldBe inspirationWidget.title
        data.type shouldBe inspirationWidget.type
        data.position shouldBe inspirationWidget.position
        data.optionSizeData.size shouldBe inspirationWidget.inspirationWidgetOptions.size
        data.optionCardData.size shouldBe 0

        inspirationWidget.inspirationWidgetOptions.forEachIndexed { index, inspirationWidgetOption ->
            data.optionSizeData[index].assertInspirationCardOptionViewModel(
                inspirationWidgetOption, data.type
            )
        }
    }

    private fun InspirationSizeOptionDataView.assertInspirationCardOptionViewModel(
        inspirationWidgetOption: SearchProductModel.InspirationCardOption,
        type: String
    ) {
        text shouldBe inspirationWidgetOption.text
        img shouldBe inspirationWidgetOption.img
        url shouldBe inspirationWidgetOption.url
        applink shouldBe inspirationWidgetOption.applink
        hexColor shouldBe inspirationWidgetOption.color
        inspirationCardType shouldBe type
        filters.key shouldBe inspirationWidgetOption.filters.key
        filters.name shouldBe inspirationWidgetOption.filters.name
        filters.value shouldBe inspirationWidgetOption.filters.value
    }

    private fun `When Load More`() {
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }

        productListPresenter.loadMoreData(searchParameter)
    }

    private fun `Then verify view add product list`() {
        verifyOrder {
            productListView.addProductList(capture(visitableListSlot))
        }
    }

    private fun `Then verify visitable list has correct inspiration size and product sequence after load more`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured
        val inspirationWidget = searchProductModel.searchInspirationWidget.data.filter { it.type == "size_perso" }

        // 0 -> product
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> product
        // 5 -> product
        // 6 -> separator
        // 7 -> inspiration size (position 20)
        // 8 -> separator
        // 9 -> product
        // 10 -> product

        visitableList.size shouldBe 11

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                6 -> {
                    visitable.shouldBeInstanceOf<SeparatorDataView>(
                        "visitable list at index $index should be Separator"
                    )
                }
                7 -> {
                    visitable.shouldBeInstanceOf<InspirationSizeDataView>(
                        "visitable list at index $index should be InspirationCardViewModel"
                    )
                    (visitable as InspirationSizeDataView).assertInspirationCardViewModel(inspirationWidget[1])
                }
                8 -> {
                    visitable.shouldBeInstanceOf<SeparatorDataView>(
                        "visitable list at index $index should be Separator"
                    )
                }
                else -> visitable.shouldBeInstanceOf<ProductItemDataView>(
                    "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    private fun `Then init size option filter and set default selected`(dataView: List<InspirationSizeDataView>) {
        verify {
            productListView.initSizeOptionFilter(dataView.toMutableList())
            productListView.setSelectedSizeOption(dataView.toMutableList())
        }
    }
}