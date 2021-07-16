package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.listShouldBe
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.result.presentation.model.BroadMatchProduct
import com.tokopedia.search.result.presentation.model.DynamicCarouselProduct
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.SeparatorDataView
import com.tokopedia.search.result.presentation.model.SuggestionDataView
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
private const val chips = "searchproduct/inspirationcarousel/chips.json"
private const val dynamicProduct = "searchproduct/inspirationcarousel/dynamic-product.json"
private const val keywordProduct = "searchproduct/inspirationcarousel/keyword-product.json"

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
                    visitable.shouldBeInstanceOf<InspirationCarouselDataView>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    assert((visitable as InspirationCarouselDataView).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO) {
                        "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO}"
                    }
                    visitable.assertInspirationCarouselDataView(searchProductModel.searchInspirationCarousel.data[1])
                }
                9 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselDataView>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    assert((visitable as InspirationCarouselDataView).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST) {
                        "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST}"
                    }
                    visitable.assertInspirationCarouselDataView(searchProductModel.searchInspirationCarousel.data[2])
                }
                14 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselDataView>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    assert((visitable as InspirationCarouselDataView).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID) {
                        "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID}"
                    }
                    visitable.assertInspirationCarouselDataView(searchProductModel.searchInspirationCarousel.data[3])
                    visitable.assertCarouselGridHasBanner(true)
                }
                else -> {
                    visitable.shouldBeInstanceOf<ProductItemDataView>(
                            "visitable list at index $index should be ProductItemViewModel"
                    )
                }
            }
        }
    }

    private fun InspirationCarouselDataView.assertInspirationCarouselDataView(inspirationCarouselData: SearchProductModel.InspirationCarouselData) {
        this.layout shouldBe inspirationCarouselData.layout
        this.type shouldBe inspirationCarouselData.type
        this.position shouldBe inspirationCarouselData.position
        this.title shouldBe inspirationCarouselData.title

        var expectedOptionPosition = 1
        this.options.listShouldBe(inspirationCarouselData.inspirationCarouselOptions) { actual, expected ->
            actual.title shouldBe expected.title
            actual.url shouldBe expected.url
            actual.applink shouldBe expected.applink
            actual.bannerImageUrl shouldBe expected.bannerImageUrl
            actual.bannerLinkUrl shouldBe expected.bannerLinkUrl
            actual.bannerApplinkUrl shouldBe expected.bannerApplinkUrl
            actual.product.assert(expected.inspirationCarouselProducts, this.type, this.layout, expectedOptionPosition, expected.title)
            actual.inspirationCarouselType shouldBe this.type
            actual.layout shouldBe this.layout
            actual.position shouldBe this.position
            actual.carouselTitle shouldBe this.title
            actual.optionPosition shouldBe expectedOptionPosition

            expectedOptionPosition++
        }
    }

    private fun InspirationCarouselDataView.assertCarouselGridHasBanner(shouldAddBannerCard: Boolean) {
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
                visitable.shouldBeInstanceOf<InspirationCarouselDataView>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                )
                assert((visitable as InspirationCarouselDataView).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID) {
                    "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID}"
                }
                visitable.assertInspirationCarouselDataView(searchProductModel.searchInspirationCarousel.data[4])
                visitable.assertCarouselGridHasBanner(false)
            }
            else if (index == 7) {
                visitable.shouldBeInstanceOf<InspirationCarouselDataView>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                )
                assert((visitable as InspirationCarouselDataView).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO) {
                    "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO}"
                }
                visitable.assertInspirationCarouselDataView(searchProductModel.searchInspirationCarousel.data[5])
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemDataView>(
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

        visitableList.any { it is InspirationCarouselDataView } shouldBe false
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
                visitable.shouldBeInstanceOf<InspirationCarouselDataView>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                )
                (visitable as InspirationCarouselDataView).assertInspirationCarouselDataView(searchProductModel.searchInspirationCarousel.data[0])
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemDataView>(
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
                    visitable.shouldBeInstanceOf<InspirationCarouselDataView>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    (visitable as InspirationCarouselDataView).assertInspirationCarouselDataView(searchProductModel.searchInspirationCarousel.data[1])
                }
                9 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselDataView>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    (visitable as InspirationCarouselDataView).assertInspirationCarouselDataView(searchProductModel.searchInspirationCarousel.data[2])
                }
                else -> {
                    visitable.shouldBeInstanceOf<ProductItemDataView>(
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
                    visitable.shouldBeInstanceOf<InspirationCarouselDataView>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    (visitable as InspirationCarouselDataView).assertInspirationCarouselDataView(searchProductModel.searchInspirationCarousel.data[3])
                }
                9 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselDataView>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    (visitable as InspirationCarouselDataView).assertInspirationCarouselDataView(searchProductModel.searchInspirationCarousel.data[4])
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
                    visitable.shouldBeInstanceOf<InspirationCarouselDataView>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    (visitable as InspirationCarouselDataView).assertInspirationCarouselDataView(searchProductModel.searchInspirationCarousel.data[1])
                }
                5 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselDataView>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    (visitable as InspirationCarouselDataView).assertInspirationCarouselDataView(searchProductModel.searchInspirationCarousel.data[0])
                }
                14 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselDataView>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    (visitable as InspirationCarouselDataView).assertInspirationCarouselDataView(searchProductModel.searchInspirationCarousel.data[2])
                }
                else -> {
                    visitable.shouldBeInstanceOf<ProductItemDataView>(
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
            visitable.shouldBeInstanceOf<ProductItemDataView>(
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
                visitable.shouldBeInstanceOf<InspirationCarouselDataView>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                )
                assert((visitable as InspirationCarouselDataView).layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO) {
                    "Inspiration Carousel layout should be ${SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO}"
                }
            }
            else {
                visitable.shouldBeInstanceOf<ProductItemDataView>(
                        "visitable list at index $index should be ProductItemViewModel"
                )
            }
        }
    }

    @Test
    fun `Show inspiration carousel chips`() {
        val searchProductModel = chips.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)

        `When Load Data`()

        `Then verify view set product list`()
        `Then assert inspiration carousel chips`(searchProductModel)
    }

    private fun `Then assert inspiration carousel chips`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured
        val searchInspirationCarousel = searchProductModel.searchInspirationCarousel

        visitableList.forEachIndexed { index, visitable ->
            // Position 6 should not be rendered because no product list
            when (index) {
                4 -> {
                    val expectedInspirationCarousel = searchInspirationCarousel.data[0]
                    visitable.assertInspirationCarouselChips(index, expectedInspirationCarousel)
                }
                9 -> {
                    val expectedInspirationCarousel = searchInspirationCarousel.data[2]
                    visitable.assertInspirationCarouselChips(index, expectedInspirationCarousel)
                    visitable.assertInspirationCarouselHexColor(expectedInspirationCarousel)
                }
                12 -> {
                    val expectedInspirationCarousel = searchInspirationCarousel.data[3]
                    visitable.assertInspirationCarouselChips(index, expectedInspirationCarousel)
                    visitable.assertInspirationCarouselChipImageUrl(expectedInspirationCarousel)
                }
                else -> {
                    (visitable is InspirationCarouselDataView).shouldBe(
                            false,
                            "Visitable index $index should not be inspiration carousel"
                    )
                }
            }
        }
    }

    private fun Visitable<*>.assertInspirationCarouselChips(
            index: Int,
            expectedInspirationCarousel: SearchProductModel.InspirationCarouselData,
    ) {
        shouldBeInstanceOf<InspirationCarouselDataView>(
                "visitable list at index $index should be InspirationCarouselViewModel"
        )
        assert((this as InspirationCarouselDataView).layout == LAYOUT_INSPIRATION_CAROUSEL_CHIPS) {
            "Inspiration Carousel layout should be $LAYOUT_INSPIRATION_CAROUSEL_CHIPS"
        }

        assertInspirationCarouselDataView(expectedInspirationCarousel)

        options.forEachIndexed { optionIndex, option ->
            val expectedOption = expectedInspirationCarousel.inspirationCarouselOptions[optionIndex]
            option.identifier shouldBe expectedOption.identifier
            option.isChipsActive shouldBe (optionIndex == 0)
        }
    }

    private fun Visitable<*>.assertInspirationCarouselHexColor(
            expectedInspirationCarousel: SearchProductModel.InspirationCarouselData
    ) {
        if (this !is InspirationCarouselDataView)
            throw AssertionError("Not inspiration carousel data view")

        options.forEachIndexed { optionIndex, option ->
            val expectedOption = expectedInspirationCarousel.inspirationCarouselOptions[optionIndex]

            option.hexColor shouldBe expectedOption.meta
            option.chipImageUrl shouldBe ""
        }
    }

    private fun Visitable<*>.assertInspirationCarouselChipImageUrl(
            expectedInspirationCarousel: SearchProductModel.InspirationCarouselData
    ) {
        if (this !is InspirationCarouselDataView)
            throw AssertionError("Not inspiration carousel data view")

        options.forEachIndexed { optionIndex, option ->
            val expectedOption = expectedInspirationCarousel.inspirationCarouselOptions[optionIndex]

            option.chipImageUrl shouldBe expectedOption.meta
            option.hexColor shouldBe ""
        }
    }

    @Test
    fun `Show inspiration carousel dynamic product`() {
        val searchProductModel = dynamicProduct.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)

        `When Load Data`()

        `Then verify view set product list`()
        `Then assert inspiration carousel product list`(
                searchProductModel,
                ::assertCarouselProductTypeDynamic
        )
    }

    private fun `Then assert inspiration carousel product list`(
            searchProductModel: SearchProductModel,
            assertType: (BroadMatchDataView, SearchProductModel.InspirationCarouselData) -> Unit,
    ) {
        /**
         * Dynamic Carousel Product feature has same UI with Broad Match
         * */
        val inspirationCarouselData = searchProductModel.searchInspirationCarousel.findDynamicProduct()
        val visitableList = visitableListSlot.captured
        val firstProductPosition = visitableList.indexOfFirst { it is ProductItemDataView }
        val assertionData = CarouselProductListAssertionData(
                firstProductPosition,
                inspirationCarouselData,
                assertType
        )

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                assertionData.topSeparatorPosition, assertionData.bottomSeparatorPosition ->
                    visitable.shouldBeInstanceOf<SeparatorDataView>()
                in assertionData.inspirationCarouselRange ->
                    assertInspirationCarouselAsBroadMatch(index, assertionData, visitable)
                else ->
                    visitable.assertNotBroadMatchDataView(index)
            }
        }
    }

    private fun SearchProductModel.SearchInspirationCarousel.findDynamicProduct(): SearchProductModel.InspirationCarouselData {
        return data.find {
            it.layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_DYNAMIC_PRODUCT
        } ?: throw AssertionError("Dynamic Product Carousel not found")
    }

    class CarouselProductListAssertionData(
            firstProductPosition: Int,
            val inspirationCarouselData: SearchProductModel.InspirationCarouselData,
            val assertType: (BroadMatchDataView, SearchProductModel.InspirationCarouselData) -> Unit,
    ) {
        val topSeparatorPosition =
                firstProductPosition + inspirationCarouselData.position
        val bottomSeparatorPosition =
                topSeparatorPosition + inspirationCarouselData.inspirationCarouselOptions.size + 2
        val inspirationCarouselRange =
                (topSeparatorPosition + 1) until bottomSeparatorPosition

        var inspirationCarouselOptionIndex = 0
    }

    private fun assertInspirationCarouselAsBroadMatch(
            index: Int,
            assertionData: CarouselProductListAssertionData,
            visitable: Visitable<*>,
    ) {
        val inspirationCarouselData = assertionData.inspirationCarouselData
        val assertType = assertionData.assertType

        if (index == assertionData.inspirationCarouselRange.first) {
            visitable.assertSuggestionViewModel(inspirationCarouselData)
        }
        else {
            visitable.assertInspirationCarouselAsBroadMatch(
                    index,
                    assertionData.inspirationCarouselOptionIndex,
                    inspirationCarouselData
            )

            val broadMatchDataView = visitable as BroadMatchDataView
            assertType(broadMatchDataView, inspirationCarouselData)

            assertionData.inspirationCarouselOptionIndex++
        }
    }

    private fun Visitable<*>.assertSuggestionViewModel(
            inspirationCarouselData: SearchProductModel.InspirationCarouselData,
    ) {
        val suggestionDataView = this as SuggestionDataView

        suggestionDataView.suggestionText shouldBe inspirationCarouselData.title
    }

    private fun Visitable<*>.assertInspirationCarouselAsBroadMatch(
            visitableIndex: Int,
            inspirationCarouselOptionIndex: Int,
            inspirationCarouselData: SearchProductModel.InspirationCarouselData,
    ) {
        val shouldBeBroadMatchMessage = "Visitable list at index $visitableIndex should be Broad Match Data View"
        val inspirationCarouselOption = inspirationCarouselData.inspirationCarouselOptions[inspirationCarouselOptionIndex]

        this.shouldBeInstanceOf<BroadMatchDataView>(shouldBeBroadMatchMessage)
        (this as BroadMatchDataView).assertBroadMatchViewModel(inspirationCarouselOption)
    }

    private fun BroadMatchDataView.assertBroadMatchViewModel(
            inspirationCarouselOption: SearchProductModel.InspirationCarouselOption,
    ) {
        keyword shouldBe inspirationCarouselOption.title
        applink shouldBe inspirationCarouselOption.applink

        val inspirationCarouselProducts = inspirationCarouselOption.inspirationCarouselProducts
        broadMatchItemDataViewList.size shouldBe inspirationCarouselProducts.size

        inspirationCarouselProducts.forEachIndexed { index, inspirationCarouselProduct ->
            broadMatchItemDataViewList[index].assertBroadMatchItemViewModel(
                    index,
                    inspirationCarouselProduct,
                    inspirationCarouselOption.title,
            )
        }
    }

    private fun BroadMatchItemDataView.assertBroadMatchItemViewModel(
            index: Int,
            inspirationCarouselProduct: SearchProductModel.InspirationCarouselProduct,
            expectedAlternativeKeyword: String,
    ) {
        id shouldBe inspirationCarouselProduct.id
        name shouldBe inspirationCarouselProduct.name
        price shouldBe inspirationCarouselProduct.price
        imageUrl shouldBe inspirationCarouselProduct.imgUrl
        url shouldBe inspirationCarouselProduct.url
        applink shouldBe inspirationCarouselProduct.applink
        priceString shouldBe inspirationCarouselProduct.priceStr
        ratingAverage shouldBe inspirationCarouselProduct.ratingAverage
        shopLocation shouldBe inspirationCarouselProduct.shop.city
        position shouldBe index + 1
        alternativeKeyword shouldBe expectedAlternativeKeyword

        labelGroupDataList.listShouldBe(inspirationCarouselProduct.labelGroupList) { actual, expected ->
            actual.title shouldBe expected.title
            actual.position shouldBe expected.position
            actual.type shouldBe expected.type
            actual.imageUrl shouldBe expected.url
        }

        badgeItemDataViewList.listShouldBe(inspirationCarouselProduct.badgeList) { actual, expected ->
            actual.title shouldBe  expected.title
            actual.imageUrl shouldBe expected.imageUrl
            actual.isShown shouldBe expected.isShown
        }
    }

    private fun Visitable<*>.assertNotBroadMatchDataView(visitableIndex: Int) {
        (this is BroadMatchDataView).shouldBe(
                false,
                "Visitable index $visitableIndex should not be broad match"
        )
    }

    private fun assertCarouselProductTypeDynamic(
            broadMatchDataView: BroadMatchDataView,
            inspirationCarouselData: SearchProductModel.InspirationCarouselData,
    ) {
        broadMatchDataView.broadMatchItemDataViewList.forEach {
            val carouselProductType = it.carouselProductType
            carouselProductType.shouldBeInstanceOf<DynamicCarouselProduct>()

            val dynamicCarouselProductType = carouselProductType as DynamicCarouselProduct
            dynamicCarouselProductType.hasThreeDots shouldBe false
            dynamicCarouselProductType.type shouldBe inspirationCarouselData.type
        }
    }

    @Test
    fun `Show inspiration carousel keyword product`() {
        val searchProductModel = keywordProduct.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)

        `When Load Data`()

        `Then verify view set product list`()
        `Then assert inspiration carousel product list`(
                searchProductModel,
                ::assertCarouselProductTypeBroadMatch,
        )
    }

    private fun assertCarouselProductTypeBroadMatch(
            broadMatchDataView: BroadMatchDataView,
            inspirationCarouselData: SearchProductModel.InspirationCarouselData,
    ) {
        broadMatchDataView.broadMatchItemDataViewList.forEach {
            val carouselProductType = it.carouselProductType
            carouselProductType.shouldBeInstanceOf<BroadMatchProduct>()

            val broadMatchProductType = carouselProductType as BroadMatchProduct
            broadMatchProductType.isOrganicAds shouldBe false
            broadMatchProductType.hasThreeDots shouldBe false
        }
    }
}