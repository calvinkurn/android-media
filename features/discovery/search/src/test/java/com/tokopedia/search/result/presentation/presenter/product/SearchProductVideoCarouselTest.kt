package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.listShouldBe
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.inspirationcarousel.LAYOUT_INSPIRATION_CAROUSEL_VIDEO
import com.tokopedia.search.result.product.videowidget.InspirationCarouselVideoDataView
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verifyOrder
import org.junit.Test
import rx.Subscriber

private const val videoWidget = "searchproduct/videowidget/videowidget.json"

internal class SearchProductVideoCarouselTest: ProductListPresenterTestFixtures()  {
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val keyword = "berrybenka sandal"
    private val searchParameter = mapOf<String, Any>(
        SearchApiConst.Q to keyword,
        SearchApiConst.START to "0",
        SearchApiConst.UNIQUE_ID to "unique_id",
        SearchApiConst.USER_ID to "0",
    )
    private val expectedDimension90 = Dimension90Utils.getDimension90(searchParameter)

    @Test
    fun `Show video widget`() {
        val searchProductModel: SearchProductModel = videoWidget.jsonToObject()

        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has video widget and product sequence on first page`(searchProductModel)
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
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify view set product list`() {
        verifyOrder {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Then verify visitable list has video widget and product sequence on first page`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured

        // 0 -> choose address data
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> product
        // 5 -> product
        // 6 -> product
        // 7 -> product
        // 8 -> product
        // 9 -> video widget (position 8)
        // 10 -> product
        // 11 -> product
        // 12 -> product
        // 13 -> product
        // 14 -> product
        // 15 -> product
        visitableList.size shouldBe 16

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                0 -> {
                    visitable.shouldBeInstanceOf<ChooseAddressDataView>(
                        "visitable list at index $index should be ChooseAddressDataViewModel"
                    )
                }
                9 -> {
                    visitable.shouldBeInstanceOf<InspirationCarouselVideoDataView>(
                        "visitable list at index $index should be InspirationCarouselVideoViewModel"
                    )
                    assert((visitable as InspirationCarouselVideoDataView).data.layout == LAYOUT_INSPIRATION_CAROUSEL_VIDEO) {
                        "Inspiration Carousel layout should be $LAYOUT_INSPIRATION_CAROUSEL_VIDEO"
                    }
                    visitable.data.assertInspirationCarouselDataView(searchProductModel.searchInspirationCarousel.data[0])
                }
                else -> {
                    visitable.shouldBeInstanceOf<ProductItemDataView>(
                        "visitable list at index $index should be ProductItemViewModel"
                    )
                }
            }
        }
    }

    private fun InspirationCarouselDataView.assertInspirationCarouselDataView(
        inspirationCarouselData: SearchProductModel.InspirationCarouselData
    ) {
        this.layout shouldBe inspirationCarouselData.layout
        this.type shouldBe inspirationCarouselData.type
        this.position shouldBe inspirationCarouselData.position
        this.title shouldBe inspirationCarouselData.title
        this.trackingOption shouldBe inspirationCarouselData.trackingOption.toInt()

        var expectedOptionPosition = 1
        this.options.listShouldBe(inspirationCarouselData.inspirationCarouselOptions) { actual, expected ->
            actual.title shouldBe expected.title
            actual.url shouldBe expected.url
            actual.applink shouldBe expected.applink
            actual.bannerImageUrl shouldBe expected.bannerImageUrl
            actual.bannerLinkUrl shouldBe expected.bannerLinkUrl
            actual.bannerApplinkUrl shouldBe expected.bannerApplinkUrl
            actual.componentId shouldBe expected.componentId
            actual.product.assert(
                expected.inspirationCarouselProducts,
                this.title,
                this.type,
                this.layout,
                expectedOptionPosition,
                expected.title,
                expectedDimension90,
            )
            actual.inspirationCarouselType shouldBe this.type
            actual.layout shouldBe this.layout
            actual.position shouldBe this.position
            actual.carouselTitle shouldBe this.title
            actual.trackingOption shouldBe this.trackingOption
            actual.optionPosition shouldBe expectedOptionPosition
            actual.dimension90 shouldBe expectedDimension90

            expectedOptionPosition++
        }
    }
}
