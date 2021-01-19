package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.listShouldBe
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
private const val unknownLayout = "searchproduct/inspirationcarousel/unknown-layout.json"

internal class SearchProductInspirationCarouselTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Show inspiration carousel general cases`() {
        val searchProductModel: SearchProductModel = inFirstPage.jsonToObject()
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration carousel and product sequence on first page`(searchProductModel)

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list has correct inspiration carousel and product sequence after load more`(searchProductModel)
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

    private fun `Then verify visitable list has correct inspiration carousel and product sequence on first page`(searchProductModel: SearchProductModel) {
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
        // 14 -> inspiration carousel grid (position 12)
        // 15 -> product
        // 16 -> product
        visitableList.size shouldBe 17

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                4 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    assert((visitable as InspirationCarouselViewModel).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO) {
                        "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO}"
                    }
                    visitable.assertInspirationCarouselViewModel(searchProductModel.searchInspirationCarousel.data[1])
                }
                9 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    assert((visitable as InspirationCarouselViewModel).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST) {
                        "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST}"
                    }
                    visitable.assertInspirationCarouselViewModel(searchProductModel.searchInspirationCarousel.data[2])
                }
                14 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    assert((visitable as InspirationCarouselViewModel).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID) {
                        "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID}"
                    }
                    visitable.assertInspirationCarouselViewModel(searchProductModel.searchInspirationCarousel.data[3])
                    visitable.assertCarouselGridHasBanner(true)
                }
                else -> {
                    visitable.shouldBeInstanceOf<ProductItemViewModel>(
                            "visitable list at index $index should be ProductItemViewModel"
                    )
                }
            }
        }
    }

    private fun InspirationCarouselViewModel.assertInspirationCarouselViewModel(inspirationCarouselData: SearchProductModel.InspirationCarouselData) {
        this.layout shouldBe inspirationCarouselData.layout
        this.type shouldBe inspirationCarouselData.type
        this.position shouldBe inspirationCarouselData.position
        this.title shouldBe inspirationCarouselData.title
        this.options.listShouldBe(inspirationCarouselData.inspirationCarouselOptions) { actual, expected ->
            actual.title shouldBe expected.title
            actual.url shouldBe expected.url
            actual.applink shouldBe expected.applink
            actual.bannerImageUrl shouldBe expected.bannerImageUrl
            actual.bannerLinkUrl shouldBe expected.bannerLinkUrl
            actual.bannerApplinkUrl shouldBe expected.bannerApplinkUrl
            actual.product.listShouldBe(expected.inspirationCarouselProducts) { actualProduct, expectedProduct ->
                actualProduct.id shouldBe expectedProduct.id
                actualProduct.name shouldBe expectedProduct.name
                actualProduct.price shouldBe expectedProduct.price
                actualProduct.priceStr shouldBe expectedProduct.priceStr
                actualProduct.imgUrl shouldBe expectedProduct.imgUrl
                actualProduct.rating shouldBe expectedProduct.rating
                actualProduct.countReview shouldBe expectedProduct.countReview
                actualProduct.url shouldBe expectedProduct.url
                actualProduct.applink shouldBe expectedProduct.applink
                actualProduct.description shouldBe expectedProduct.description
                actualProduct.inspirationCarouselType shouldBe this.type
                actualProduct.ratingAverage shouldBe expectedProduct.ratingAverage
                actualProduct.labelGroupList shouldBe expectedProduct.labelGroupList
                actualProduct.layout shouldBe this.layout
                actualProduct.originalPrice shouldBe expectedProduct.originalPrice
                actualProduct.discountPercentage shouldBe expectedProduct.discountPercentage
            }
            actual.inspirationCarouselType shouldBe this.type
            actual.layout shouldBe this.layout
            actual.position shouldBe this.position
            actual.carouselTitle shouldBe this.title
        }
    }

    private fun InspirationCarouselViewModel.assertCarouselGridHasBanner(shouldAddBannerCard: Boolean) {
        this.options[0].shouldAddBannerCard() shouldBe shouldAddBannerCard
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

    private fun `Then verify visitable list has correct inspiration carousel and product sequence after load more`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured

        // 0 -> product
        // 1 -> product
        // 2 -> inspiration carousel grid no banner (position 16)
        // 3 -> product
        // 4 -> product
        // 5 -> product
        // 6 -> product
        // 7 -> inspiration carousel info (position 20)
        // 8 -> product
        // 9 -> product
        visitableList.size shouldBe 10

        visitableList.forEachIndexed { index, visitable ->
            if (index == 2) {
                visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                )
                assert((visitable as InspirationCarouselViewModel).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID) {
                    "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID}"
                }
                visitable.assertInspirationCarouselViewModel(searchProductModel.searchInspirationCarousel.data[4])
                visitable.assertCarouselGridHasBanner(false)
            }
            else if (index == 7) {
                visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                )
                assert((visitable as InspirationCarouselViewModel).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO) {
                    "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO}"
                }
                visitable.assertInspirationCarouselViewModel(searchProductModel.searchInspirationCarousel.data[5])
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
        val searchProductModel: SearchProductModel = inPosition9.jsonToObject()
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify inspiration carousel is not shown on first page`()

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list has inspiration carousel after 9th product item`(searchProductModel)
    }

    private fun `Then verify inspiration carousel is not shown on first page`() {
        val visitableList = visitableListSlot.captured

        visitableList.any { it is InspirationCarouselViewModel } shouldBe false
    }

    private fun `Then verify visitable list has inspiration carousel after 9th product item`(searchProductModel: SearchProductModel) {
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
                (visitable as InspirationCarouselViewModel).assertInspirationCarouselViewModel(searchProductModel.searchInspirationCarousel.data[0])
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
        val searchProductModel: SearchProductModel = inFirstPageNoTopads.jsonToObject()
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration carousel position for search result first page without Top Ads product`(searchProductModel)

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list has correct inspiration carousel position for search result next pages without Top Ads product`(inFirstPageNoTopads.jsonToObject())
    }

    private fun `Then verify visitable list has correct inspiration carousel position for search result first page without Top Ads product`(searchProductModel: SearchProductModel) {
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
            when (index) {
                4 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    (visitable as InspirationCarouselViewModel).assertInspirationCarouselViewModel(searchProductModel.searchInspirationCarousel.data[1])
                }
                9 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    (visitable as InspirationCarouselViewModel).assertInspirationCarouselViewModel(searchProductModel.searchInspirationCarousel.data[2])
                }
                else -> {
                    visitable.shouldBeInstanceOf<ProductItemViewModel>(
                            "visitable list at index $index should be ProductItemViewModel"
                    )
                }
            }
        }
    }

    private fun `Then verify visitable list has correct inspiration carousel position for search result next pages without Top Ads product`(searchProductModel: SearchProductModel) {
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
            when (index) {
                4 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    (visitable as InspirationCarouselViewModel).assertInspirationCarouselViewModel(searchProductModel.searchInspirationCarousel.data[3])
                }
                9 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    (visitable as InspirationCarouselViewModel).assertInspirationCarouselViewModel(searchProductModel.searchInspirationCarousel.data[4])
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
    fun `Show two inspiration carousel with same position`() {
        val searchProductModel: SearchProductModel = samePosition.jsonToObject()
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration carousel in the same position`(searchProductModel)

        `When Load More`()

        `Then verify view add product list`()
        `Then verify visitable list only has product items`()
    }

    private fun `Then verify visitable list has correct inspiration carousel in the same position`(searchProductModel: SearchProductModel) {
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
            when (index) {
                4 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    (visitable as InspirationCarouselViewModel).assertInspirationCarouselViewModel(searchProductModel.searchInspirationCarousel.data[1])
                }
                5 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    (visitable as InspirationCarouselViewModel).assertInspirationCarouselViewModel(searchProductModel.searchInspirationCarousel.data[0])
                }
                14 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselViewModel>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    (visitable as InspirationCarouselViewModel).assertInspirationCarouselViewModel(searchProductModel.searchInspirationCarousel.data[2])
                }
                else -> {
                    visitable.shouldBeInstanceOf<ProductItemViewModel>(
                            "visitable list at index $index should be ProductItemViewModel"
                    )
                }
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
    fun `Hide Inspiration Carousel with unknown layout type`() {
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(unknownLayout.jsonToObject())
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list does not render unknown carousel layout`()
    }

    private fun `Then verify visitable list does not render unknown carousel layout`() {
        val visitableList = visitableListSlot.captured

        // 0 -> product
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> product
        // 5 -> product
        // 6 -> product
        // 7 -> product
        // 8 -> inspiration carousel list (position 8)
        // 9 -> product
        // 10 -> product
        // 11 -> product
        // 12 -> product
        // 13 -> product
        // 14 -> product
        visitableList.size shouldBe 15

        visitableList.forEachIndexed { index, visitable ->
            if (index == 8) {
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
}