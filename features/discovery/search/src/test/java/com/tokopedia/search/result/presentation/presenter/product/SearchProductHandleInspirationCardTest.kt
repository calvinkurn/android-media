package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.InspirationCardViewModel
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.search.shouldBe
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val inspirationCardResponseFirstPage = "searchproduct/inspirationcard/in-first-page.json"
private const val inspirationCardResponseOnlyPosition9 = "searchproduct/inspirationcard/in-position-9.json"
private const val inspirationCardResponseWithoutTopAds = "searchproduct/inspirationcard/without-topads.json"
private const val inspirationCardResponseSamePosition = "searchproduct/inspirationcard/same-position.json"
private const val inspirationCardResponseSamePositionWithCarousel = "searchproduct/inspirationcard/same-position-with-carousel.json"

internal class SearchProductHandleInspirationCardTest: ProductListPresenterTestFixtures() {
    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Show inspiration card general cases`() {
        val searchProductModel = inspirationCardResponseFirstPage.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with Inspiration Card`(searchProductModel)
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration card and product sequence on first page`()

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list has correct inspiration card and product sequence after load more`()
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

    private fun `Then verify visitable list has correct inspiration card and product sequence on first page`() {
        val visitableList = visitableListSlot.captured

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
        // 13 -> inspiration carousel (position 12)
        // 14 -> product
        // 15 -> product
        // 16 -> product
        visitableList.size shouldBe 16

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                8 -> visitable.shouldBeInstanceOf<InspirationCardViewModel>(
                        "visitable list at index $index should be InspirationCardViewModel"
                )
                13 -> visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                )
                else -> visitable.shouldBeInstanceOf<ProductItemViewModel>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
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

    private fun `Then verify visitable list has correct inspiration card and product sequence after load more`() {
        val visitableList = visitableListSlot.captured

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

        visitableList.forEachIndexed { index, visitable ->
            if (index == 2 || index == 5 || index == 8) {
                visitable.shouldBeInstanceOf<InspirationCardViewModel>(
                        "visitable list at index $index should be InspirationCardViewModel"
                )
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemViewModel>(
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
        `Then verify visitable list has inspiration card after 9th product item`()
    }

    private fun `Then verify inspiration card is not shown on first page`() {
        val visitableList = visitableListSlot.captured

        // 0 -> product
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> product
        // 5 -> product
        // 6 -> product
        // 7 -> product
        // 8 -> product

        visitableList.size shouldBe 8

        visitableList.forEachIndexed { index, visitable ->
            visitable.shouldBeInstanceOf<ProductItemViewModel>(
                    "visitable list at index $index should be ProductItemViewModel"
            )
        }
    }

    private fun `Then verify visitable list has inspiration card after 9th product item`() {
        val visitableList = visitableListSlot.captured

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
                visitable.shouldBeInstanceOf<InspirationCardViewModel>(
                        "visitable list at index $index should be InspirationCardViewModel"
                )
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemViewModel>(
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
        `Then verify visitable list has correct inspiration card position for search result first page without Top Ads product`()

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list has correct inspiration card position for search result next pages without Top Ads product`()
    }

    private fun `Then verify visitable list has correct inspiration card position for search result first page without Top Ads product`() {
        val visitableList = visitableListSlot.captured

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

        visitableList.forEachIndexed { index, visitable ->
            if (index == 4 || index == 9) {
                visitable.shouldBeInstanceOf<InspirationCardViewModel>(
                        "visitable list at index $index should be InspirationCardViewModel"
                )
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemViewModel>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    private fun `Then verify visitable list has correct inspiration card position for search result next pages without Top Ads product`() {
        val visitableList = visitableListSlot.captured

        // 0 -> product
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> inspiration carousel (position 12)
        // 5 -> product
        // 6 -> product
        // 7 -> inspiration card (position 14)
        // 8 -> product
        // 9 -> product

        visitableList.size shouldBe 10

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                4 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                }
                7 -> {
                    visitable.shouldBeInstanceOf<InspirationCardViewModel>(
                            "visitable list at index $index should be InspirationCardViewModel"
                    )
                }
                else -> {
                    visitable.shouldBeInstanceOf<ProductItemViewModel>(
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
        `Then verify visitable list has correct inspiration card in the same position`()

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list has inspiration card and product items`()
    }

    private fun `Then verify visitable list has correct inspiration card in the same position`() {
        val visitableList = visitableListSlot.captured

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
        // 16 -> inspiration carousel (position 12)
        // 17 -> product
        // 18 -> product

        visitableList.size shouldBe 19

        visitableList.forEachIndexed { index, visitable ->
            if (index == 16) {
                visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                )
            }
            else if (index == 4 || index == 5 || index == 10 || index == 13) {
                visitable.shouldBeInstanceOf<InspirationCardViewModel>(
                        "visitable list at index $index should be InspirationCardViewModel"
                )
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemViewModel>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    private fun `Then verify visitable list has inspiration card and product items`() {
        val visitableList = visitableListSlot.captured

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

        visitableList.forEachIndexed { index, visitable ->
            if (index == 2 || index == 7) {
                visitable.shouldBeInstanceOf<InspirationCardViewModel>(
                        "visitable list at index $index should be InspirationCardViewModel"
                )
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemViewModel>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    @Test
    fun `Show inspiration card with same position with inspiration carousel`() {
        val searchProductModel = inspirationCardResponseSamePositionWithCarousel.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with Inspiration Card`(searchProductModel)
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration card in the same position as inspiration carousel`()

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list only has product items`()
    }

    private fun  `Then verify visitable list has correct inspiration card in the same position as inspiration carousel`() {
        val visitableList = visitableListSlot.captured

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
        // 13 -> inspiration card (position 12)
        // 14 -> inspiration carousel (position 12)
        // 15 -> product
        // 16 -> product

        visitableList.size shouldBe 17

        visitableList.forEachIndexed { index, visitable ->
            if (index == 14) {
                visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                )
            }
            else if (index == 8 || index == 13) {
                visitable.shouldBeInstanceOf<InspirationCardViewModel>(
                        "visitable list at index $index should be InspirationCardViewModel"
                )
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemViewModel>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    private fun `Then verify visitable list only has product items`() {
        val visitableList = visitableListSlot.captured

        // 0 -> product
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> product
        // 5 -> product
        // 6 -> product
        // 7 -> product

        visitableList.size shouldBe 8

        visitableList.forEachIndexed { index, visitable ->
            visitable.shouldBeInstanceOf<ProductItemViewModel>(
                    "visitable list at index $index should be ProductItemViewModel"
            )
        }
    }
}