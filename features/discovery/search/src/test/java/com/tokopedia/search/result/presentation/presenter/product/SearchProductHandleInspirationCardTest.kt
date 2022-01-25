package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.SearchProductCountDataView
import com.tokopedia.search.result.presentation.model.SeparatorDataView
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardDataView
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardOptionDataView
import com.tokopedia.search.result.product.inspirationwidget.size.InspirationSizeDataView
import com.tokopedia.search.result.product.inspirationwidget.size.InspirationSizeOptionDataView
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsCollectionContaining.hasItems
import org.junit.Test
import rx.Subscriber

private const val inspirationCardResponseFirstPage = "searchproduct/inspirationcard/in-first-page.json"
private const val inspirationCardResponseOnlyPosition9 = "searchproduct/inspirationcard/in-position-9.json"
private const val inspirationCardResponseWithoutTopAds = "searchproduct/inspirationcard/without-topads.json"
private const val inspirationCardResponseSamePosition = "searchproduct/inspirationcard/same-position.json"
private const val inspirationSizeResponseFirstPage = "searchproduct/inspirationsize/in-first-page.json"

internal class SearchProductHandleInspirationCardTest: ProductListPresenterTestFixtures() {
    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Show inspiration card general cases`() {
        val searchProductModel = inspirationCardResponseFirstPage.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with Inspiration Card`(searchProductModel)
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration card and product sequence on first page`(searchProductModel)

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list has correct inspiration card and product sequence after load more`(searchProductModel)
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
    }

    private fun `Then verify view set product list`() {
        verifyOrder {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Then verify visitable list has correct inspiration card and product sequence on first page`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured
        val inspirationWidget = searchProductModel.searchInspirationWidget.data.filter { it.type != "unknown_random_type" }

        // 0 -> search product count data
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> product
        // 5 -> product
        // 6 -> product
        // 7 -> product
        // 8 -> product
        // 9 -> inspiration card (position 8)
        // 10 -> product
        // 11 -> product
        // 12 -> product
        // 13 -> product
        // 14 -> product
        // 15 -> product
        // 16 -> product

        visitableList.size shouldBe 16

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                0 -> {
                    visitable.shouldBeInstanceOf<SearchProductCountDataView>(
                        "visitable list at index $index should be SearchProductCountViewModel"
                    )
                }
                9 -> {
                    visitable.shouldBeInstanceOf<InspirationCardDataView>(
                            "visitable list at index $index should be InspirationCardViewModel"
                    )
                    (visitable as InspirationCardDataView).assertInspirationSizeViewModel(inspirationWidget[0])
                }
                else -> visitable.shouldBeInstanceOf<ProductItemDataView>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }

    }

    private fun InspirationCardDataView.assertInspirationSizeViewModel(inspirationWidget: SearchProductModel.InspirationWidgetData) {
        data.title shouldBe inspirationWidget.title
        data.type shouldBe inspirationWidget.type
        data.position shouldBe inspirationWidget.position
        optionCardData.size shouldBe inspirationWidget.inspirationWidgetOptions.size

        inspirationWidget.inspirationWidgetOptions.forEachIndexed { index, inspirationWidgetOption ->
            optionCardData[index].assertInspirationSizeOptionViewModel(
                    inspirationWidgetOption, data.type
            )
        }
    }

    private fun InspirationCardOptionDataView.assertInspirationSizeOptionViewModel(
        inspirationWidgetOption: SearchProductModel.InspirationWidgetOption,
        type: String
    ) {
        text shouldBe inspirationWidgetOption.text
        img shouldBe inspirationWidgetOption.img
        url shouldBe inspirationWidgetOption.url
        applink shouldBe inspirationWidgetOption.applink
        hexColor shouldBe inspirationWidgetOption.color
        inspirationCardType shouldBe type
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

    private fun `Then verify visitable list has correct inspiration card and product sequence after load more`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured
        val inspirationWidget = searchProductModel.searchInspirationWidget.data.filter { it.type != "unknown_random_type" }

        // 0 -> product
        // 1 -> product
        // 2 -> inspiration card (position 16)
        // 3 -> product
        // 4 -> product
        // 5 -> inspiration card (position 18)
        // 6 -> product
        // 7 -> product
        // 8 -> inspiration card (position 20)
        // 9 -> product
        // 10 -> product
        visitableList.size shouldBe 11
        var i = 1

        visitableList.forEachIndexed { index, visitable ->
            if (index == 2 || index == 5 || index == 8) {
                visitable.shouldBeInstanceOf<InspirationCardDataView>(
                        "visitable list at index $index should be InspirationCardViewModel"
                )
                (visitable as InspirationCardDataView).assertInspirationSizeViewModel(inspirationWidget[i])
                i++
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemDataView>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    @Test
    fun `Show inspiration card only at position 9 (edge cases)`() {
        val searchProductModel = inspirationCardResponseOnlyPosition9.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with Inspiration Card`(searchProductModel)
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify inspiration card is not shown on first page`()

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list has inspiration card after 9th product item`(searchProductModel)
    }

    private fun `Then verify inspiration card is not shown on first page`() {
        val visitableList = visitableListSlot.captured

        visitableList.any { it is InspirationCardDataView } shouldBe false
    }

    private fun `Then verify visitable list has inspiration card after 9th product item`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured
        val inspirationWidget = searchProductModel.searchInspirationWidget.data

        // 0 -> product
        // 1 -> inspiration card
        // 2 -> product
        // 3 -> product
        // 4 -> product
        // 5 -> product
        // 6 -> product
        // 7 -> product
        // 8 -> product

        visitableList.size shouldBe 9

        visitableList.forEachIndexed { index, visitable ->
            if (index == 1) {
                visitable.shouldBeInstanceOf<InspirationCardDataView>(
                        "visitable list at index $index should be InspirationCardViewModel"
                )
                (visitable as InspirationCardDataView).assertInspirationSizeViewModel(inspirationWidget[0])
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemDataView>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    @Test
    fun `Show inspiration card without TopAds Products`() {
        val searchProductModel = inspirationCardResponseWithoutTopAds.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with Inspiration Card`(searchProductModel)
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration card position for search result first page without Top Ads product`(searchProductModel)

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list has correct inspiration card position for search result next pages without Top Ads product`(searchProductModel)
    }

    private fun `Then verify visitable list has correct inspiration card position for search result first page without Top Ads product`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured
        val inspirationWidget = searchProductModel.searchInspirationWidget.data

        // 0 -> search product count data
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> product
        // 5 -> inspiration card (position 4)
        // 6 -> product
        // 7 -> product
        // 8 -> product
        // 9 -> product
        // 10 -> inspiration card (position 8)

        visitableList.size shouldBe 11
        var i = 0

        visitableList.forEachIndexed { index, visitable ->
            if (index == 0) {
                visitable.shouldBeInstanceOf<SearchProductCountDataView>(
                        "visitable list at index $index should be SearchProductCountViewModel"
                )
            }
            else if (index == 5 || index == 10) {
                visitable.shouldBeInstanceOf<InspirationCardDataView>(
                        "visitable list at index $index should be InspirationCardViewModel"
                )
                (visitable as InspirationCardDataView).assertInspirationSizeViewModel(inspirationWidget[i])
                i++
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemDataView>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    private fun `Then verify visitable list has correct inspiration card position for search result next pages without Top Ads product`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured
        val inspirationWidget = searchProductModel.searchInspirationWidget.data

        // 0 -> product
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> product
        // 5 -> product
        // 6 -> inspiration card (position 14)
        // 7 -> product
        // 8 -> product

        visitableList.size shouldBe 9

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                6 -> {
                    visitable.shouldBeInstanceOf<InspirationCardDataView>(
                            "visitable list at index $index should be InspirationCardViewModel"
                    )
                    (visitable as InspirationCardDataView).assertInspirationSizeViewModel(inspirationWidget[2])
                }
                else -> {
                    visitable.shouldBeInstanceOf<ProductItemDataView>(
                            "visitable list at index $index should be ProductItemViewModel"
                    )
                }
            }
        }
    }

    @Test
    fun `Show two inspiration card with same position`() {
        val searchProductModel = inspirationCardResponseSamePosition.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with Inspiration Card`(searchProductModel)
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration card in the same position`(searchProductModel)

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list has inspiration card and product items`(searchProductModel)
    }

    private fun `Then verify visitable list has correct inspiration card in the same position`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured
        val inspirationWidget = searchProductModel.searchInspirationWidget.data
        val inspirationWidgetIndex = listOf(1, 0, 2, 3)

        // 0 -> search product count data
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> product
        // 5 -> inspiration card (position 4)
        // 6 -> inspiration card (position 4)
        // 7 -> product
        // 8 -> product
        // 9 -> product
        // 10 -> product
        // 11 -> inspiration card (position 8)
        // 12 -> product
        // 13 -> product
        // 14 -> inspiration card (position 10)
        // 15 -> product
        // 16 -> product
        // 17 -> product
        // 18 -> product

        visitableList.size shouldBe 19
        var i = 0

        val inspirationCardViewModelIndex = arrayOf(5, 6, 11, 14)
        visitableList.forEachIndexed { index, visitable ->
            if (index == 0) {
                visitable.shouldBeInstanceOf<SearchProductCountDataView>(
                        "visitable list at index $index should be SearchProductCountViewModel"
                )
            }
            else if (inspirationCardViewModelIndex.contains(index)) {
                visitable.shouldBeInstanceOf<InspirationCardDataView>(
                        "visitable list at index $index should be InspirationCardViewModel"
                )
                (visitable as InspirationCardDataView).assertInspirationSizeViewModel(inspirationWidget[inspirationWidgetIndex[i]])
                i++
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemDataView>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    private fun `Then verify visitable list has inspiration card and product items`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured
        val inspirationWidget = searchProductModel.searchInspirationWidget.data
        val inspirationWidgetIndex = listOf(4, 5)

        // 0 -> product
        // 1 -> product
        // 2 -> inspiration card (position 16)
        // 3 -> product
        // 4 -> product
        // 5 -> product
        // 6 -> product
        // 7 -> inspiration card (position 20)
        // 8 -> product
        // 9 -> product

        visitableList.size shouldBe 10
        var i = 0

        visitableList.forEachIndexed { index, visitable ->
            if (index == 2 || index == 7) {
                visitable.shouldBeInstanceOf<InspirationCardDataView>(
                        "visitable list at index $index should be InspirationCardViewModel"
                )
                (visitable as InspirationCardDataView).assertInspirationSizeViewModel(inspirationWidget[inspirationWidgetIndex[i]])
                i++
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemDataView>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    @Test
    fun `Show inspiration size`() {
        val searchProductModel = inspirationSizeResponseFirstPage.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with Inspiration Card`(
            searchProductModel
        )

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify init filter controller from inspiration size`(
            searchProductModel.searchInspirationWidget
        )
        `Then verify visitable list has correct inspiration size and product sequence on first page`(
            searchProductModel
        )
    }

    private fun `Then verify init filter controller from inspiration size`(
        inspirationWidget: SearchProductModel.SearchInspirationWidget
    ) {
        val actualFilterListSlot = slot<List<Filter>>()

        verify {
            productListView.initFilterController(capture(actualFilterListSlot))
        }

        // Comparing Option list because Filter class does not have `equals` function
        val actualOptionList = actualFilterListSlot.captured.map { it.options }.flatten()
        val expectedOptionList = inspirationWidget.filters().map { it.options }.flatten()
        assertThat(actualOptionList, hasItems(*expectedOptionList.toTypedArray()))
    }

    private fun `Then verify visitable list has correct inspiration size and product sequence on first page`(
        searchProductModel: SearchProductModel
    ) {
        val visitableList = visitableListSlot.captured
        val inspirationWidget = searchProductModel.searchInspirationWidget.data

        // 0 -> search product count data
        // 1 -> separator
        // 2 -> inspiration size (position 0)
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
        // 18 -> separator
        // 19 -> inspiration size (position 14)
        // 20 -> separator

        visitableList.size shouldBe 21

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                0 -> {
                    visitable.shouldBeInstanceOf<SearchProductCountDataView>(
                        "visitable list at index $index should be SearchProductCountViewModel"
                    )
                }
                1, 3, 18, 20 -> {
                    visitable.shouldBeInstanceOf<SeparatorDataView>(
                        "visitable list at index $index should be Separator"
                    )
                }
                2 -> {
                    visitable.shouldBeInstanceOf<InspirationSizeDataView>(
                        "visitable list at index $index should be InspirationSizeViewModel"
                    )
                    (visitable as InspirationSizeDataView).assertInspirationSizeViewModel(inspirationWidget[0])
                }
                19 -> {
                    visitable.shouldBeInstanceOf<InspirationSizeDataView>(
                        "visitable list at index $index should be InspirationSizeViewModel"
                    )
                    (visitable as InspirationSizeDataView).assertInspirationSizeViewModel(inspirationWidget[1])
                }
                else -> visitable.shouldBeInstanceOf<ProductItemDataView>(
                    "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    private fun InspirationSizeDataView.assertInspirationSizeViewModel(
        inspirationWidget: SearchProductModel.InspirationWidgetData,
    ) {
        data.title shouldBe inspirationWidget.title
        data.type shouldBe inspirationWidget.type
        data.position shouldBe inspirationWidget.position
        optionSizeData.size shouldBe inspirationWidget.inspirationWidgetOptions.size

        inspirationWidget.inspirationWidgetOptions.forEachIndexed { index, inspirationWidgetOption ->
            optionSizeData[index].assertInspirationSizeOptionViewModel(
                inspirationWidgetOption, data.type,
            )
        }
    }

    private fun InspirationSizeOptionDataView.assertInspirationSizeOptionViewModel(
        inspirationWidgetOption: SearchProductModel.InspirationWidgetOption,
        type: String,
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
}