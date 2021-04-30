package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.InspirationCardOptionDataView
import com.tokopedia.search.result.presentation.model.InspirationCardDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.search.shouldBe
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val inspirationCardResponseFirstPage = "searchproduct/inspirationcard/in-first-page.json"
private const val inspirationCardResponseOnlyPosition9 = "searchproduct/inspirationcard/in-position-9.json"
private const val inspirationCardResponseWithoutTopAds = "searchproduct/inspirationcard/without-topads.json"
private const val inspirationCardResponseSamePosition = "searchproduct/inspirationcard/same-position.json"

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

        // 0 -> product
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> product
        // 5 -> product
        // 6 -> product
        // 7 -> product
        // 8 -> inspiration card (position 8)
        // 9 -> product
        // 10 -> product
        // 11 -> product
        // 12 -> product
        // 13 -> product
        // 14 -> product
        // 15 -> product

        visitableList.size shouldBe 15

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                8 -> {
                    visitable.shouldBeInstanceOf<InspirationCardDataView>(
                            "visitable list at index $index should be InspirationCardViewModel"
                    )
                    (visitable as InspirationCardDataView).assertInspirationCardViewModel(inspirationWidget[0])
                }
                else -> visitable.shouldBeInstanceOf<ProductItemDataView>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }

    }

    private fun InspirationCardDataView.assertInspirationCardViewModel(inspirationWidget: SearchProductModel.InspirationCardData) {
        title shouldBe inspirationWidget.title
        type shouldBe inspirationWidget.type
        position shouldBe inspirationWidget.position
        optionData.size shouldBe inspirationWidget.inspiratioWidgetOptions.size

        inspirationWidget.inspiratioWidgetOptions.forEachIndexed { index, inspirationWidgetOption ->
            optionData[index].assertInspirationCardOptionViewModel(
                    inspirationWidgetOption, type
            )
        }
    }

    private fun InspirationCardOptionDataView.assertInspirationCardOptionViewModel(
            inspirationWidgetOption: SearchProductModel.InspirationCardOption,
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
                (visitable as InspirationCardDataView).assertInspirationCardViewModel(inspirationWidget[i])
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
                (visitable as InspirationCardDataView).assertInspirationCardViewModel(inspirationWidget[0])
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

        // 0 -> product
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> inspiration card (position 4)
        // 5 -> product
        // 6 -> product
        // 7 -> product
        // 8 -> product
        // 9 -> inspiration card (position 8)

        visitableList.size shouldBe 10
        var i = 0

        visitableList.forEachIndexed { index, visitable ->
            if (index == 4 || index == 9) {
                visitable.shouldBeInstanceOf<InspirationCardDataView>(
                        "visitable list at index $index should be InspirationCardViewModel"
                )
                (visitable as InspirationCardDataView).assertInspirationCardViewModel(inspirationWidget[i])
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
                    (visitable as InspirationCardDataView).assertInspirationCardViewModel(inspirationWidget[2])
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

        // 0 -> product
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> inspiration card (position 4)
        // 5 -> inspiration card (position 4)
        // 6 -> product
        // 7 -> product
        // 8 -> product
        // 9 -> product
        // 10 -> inspiration card (position 8)
        // 11 -> product
        // 12 -> product
        // 13 -> inspiration card (position 10)
        // 14 -> product
        // 15 -> product
        // 16 -> product
        // 17 -> product

        visitableList.size shouldBe 18
        var i = 0

        visitableList.forEachIndexed { index, visitable ->
            if (index == 4 || index == 5 || index == 10 || index == 13) {
                visitable.shouldBeInstanceOf<InspirationCardDataView>(
                        "visitable list at index $index should be InspirationCardViewModel"
                )
                (visitable as InspirationCardDataView).assertInspirationCardViewModel(inspirationWidget[inspirationWidgetIndex[i]])
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
                (visitable as InspirationCardDataView).assertInspirationCardViewModel(inspirationWidget[inspirationWidgetIndex[i]])
                i++
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemDataView>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }
}