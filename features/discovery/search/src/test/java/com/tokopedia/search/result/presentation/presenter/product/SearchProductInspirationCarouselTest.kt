package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.search.shouldBe
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val inFirstPage = "searchproduct/inspirationcarousel/in-first-page.json"
private const val inFirstPageNoTopads = "searchproduct/inspirationcarousel/in-first-page-no-topads.json"
private const val inPosition9 = "searchproduct/inspirationcarousel/in-position-9.json"
private const val samePosition = "searchproduct/inspirationcarousel/same-position.json"

internal class SearchProductInspirationCarouselTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Show inspiration carousel general cases`() {
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(inFirstPage.jsonToObject())
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration carousel and product sequence on first page`()

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list has correct inspiration carousel and product sequence after load more`()
    }

    private fun `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        every { searchProductLoadMoreUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductCommonResponseJSON.jsonToObject())
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

    private fun `Then verify visitable list has correct inspiration carousel and product sequence on first page`() {
        val visitableList = visitableListSlot.captured

        // 0 -> product
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> inspiration carousel info (position 4)
        // 5 -> product
        // 6 -> product
        // 7 -> product
        // 8 -> product
        // 9 -> inspiration carousel list (position 8)
        // 10 -> product
        // 11 -> product
        // 12 -> product
        // 13 -> product
        // 14 -> inspiration carousel list (position 12)
        // 15 -> product
        // 16 -> product
        visitableList.size shouldBe 17

        visitableList.forEachIndexed { index, visitable ->
            if (index == 4) {
                visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                )
                assert((visitable as InspirationCarouselViewModel).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO) {
                    "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO}"
                }
            }
            else if (index == 9 || index == 14) {
                visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                )
                assert((visitable as InspirationCarouselViewModel).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST) {
                    "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST}"
                }
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemViewModel>(
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

    private fun `Then verify visitable list has correct inspiration carousel and product sequence after load more`() {
        val visitableList = visitableListSlot.captured

        // 0 -> product
        // 1 -> product
        // 2 -> inspiration carousel (position 16)
        // 3 -> product
        // 4 -> product
        // 5 -> product
        // 6 -> product
        // 7 -> inspiration carousel (position 20)
        // 8 -> product
        // 9 -> product
        visitableList.size shouldBe 10

        visitableList.forEachIndexed { index, visitable ->
            if (index == 2) {
                visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                )
                assert((visitable as InspirationCarouselViewModel).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST) {
                    "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST}"
                }
            }
            else if (index == 7) {
                visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                )
                assert((visitable as InspirationCarouselViewModel).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO) {
                    "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO}"
                }
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemViewModel>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    @Test
    fun `Show inspiration carousel only at position 9 (edge cases)`() {
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(inPosition9.jsonToObject())
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify inspiration carousel is not shown on first page`()

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list has inspiration carousel after 9th product item`()
    }

    private fun `Then verify inspiration carousel is not shown on first page`() {
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

    private fun `Then verify visitable list has inspiration carousel after 9th product item`() {
        val visitableList = visitableListSlot.captured

        // 0 -> product
        // 1 -> inspiration carousel
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
                visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
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
    fun `Show inspiration carousel without TopAds Products`() {
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(inFirstPageNoTopads.jsonToObject())
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration carousel position for search result first page without Top Ads product`()

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list has correct inspiration carousel position for search result next pages without Top Ads product`()
    }

    private fun `Then verify visitable list has correct inspiration carousel position for search result first page without Top Ads product`() {
        val visitableList = visitableListSlot.captured

        // 0 -> product
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> inspiration carousel (position 4)
        // 5 -> product
        // 6 -> product
        // 7 -> product
        // 8 -> product
        // 9 -> inspiration carousel (position 8)

        visitableList.size shouldBe 10

        visitableList.forEachIndexed { index, visitable ->
            if (index == 4 || index == 9) {
                visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                )
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemViewModel>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    private fun `Then verify visitable list has correct inspiration carousel position for search result next pages without Top Ads product`() {
        val visitableList = visitableListSlot.captured

        // 0 -> product
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> inspiration carousel (position 12)
        // 5 -> product
        // 6 -> product
        // 7 -> product
        // 8 -> product
        // 9 -> inspiration carousel (position 16)

        visitableList.size shouldBe 10

        visitableList.forEachIndexed { index, visitable ->
            if (index == 4 || index == 9) {
                visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
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
    fun `Show two inspiration carousel with same position`() {
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(samePosition.jsonToObject())
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration carousel in the same position`()

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list only has product items`()
    }

    private fun `Then verify visitable list has correct inspiration carousel in the same position`() {
        val visitableList = visitableListSlot.captured

        // 0 -> product
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> inspiration carousel (position 4)
        // 5 -> inspiration carousel (position 4)
        // 6 -> product
        // 7 -> product
        // 8 -> product
        // 9 -> product
        // 10 -> product
        // 11 -> product
        // 12 -> product
        // 13 -> product
        // 14 -> inspiration carousel (position 12)
        // 15 -> product
        // 16 -> product

        visitableList.size shouldBe 17

        visitableList.forEachIndexed { index, visitable ->
            if (index == 4 || index == 5 || index == 14) {
                visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
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

    @Test
    fun `Tracker Impression Inspiration Carousel`() {
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(inFirstPage.jsonToObject())

        `When Load Data`()

        `Then verify view set product list`()


        // POSITION
        // 5 -> inspiration carousel info (position 4)
        // 15 -> inspiration carousel list (position 12)
        val visitableList = visitableListSlot.captured
        `Then verify interaction for Inspiration Carousel Info impression`(visitableList[5] as InspirationCarouselViewModel)
        `Then verify interaction for Inspiration Carousel List impression`(visitableList[15] as InspirationCarouselViewModel)
    }

    private fun `Then verify interaction for Inspiration Carousel Info impression`(data: InspirationCarouselViewModel) {
        verify {
            productListView.sendImpressionInspirationCarouselInfo(data)
        }
    }

    private fun `Then verify interaction for Inspiration Carousel List impression`(data: InspirationCarouselViewModel) {
        verify {
            productListView.sendImpressionInspirationCarouselList(data)
        }
    }
}