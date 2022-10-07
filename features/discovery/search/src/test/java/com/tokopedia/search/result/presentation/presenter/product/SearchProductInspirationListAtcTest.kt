package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcDataView
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcView
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.search.shouldBe
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val inspirationListAtc = "searchproduct/inspirationlistatc/inspiration-list-atc.json"

internal class SearchProductInspirationListAtcTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val keyword = "samsung"
    private val searchParameter = mapOf<String, Any>(
        SearchApiConst.Q to keyword,
        SearchApiConst.START to "0",
        SearchApiConst.UNIQUE_ID to "unique_id",
        SearchApiConst.USER_ID to "0",
    )
    private val expectedDimension90 = Dimension90Utils.getDimension90(searchParameter)

    @Test
    fun `Show inspiration list atc carousel`() {
        val searchProductModel: SearchProductModel = inspirationListAtc.jsonToObject()

        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct list atc and product sequence on first page`(searchProductModel)
    }

    private fun `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When Load Data`() {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Given Load Data`() {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify view set product list`() {
        verifyOrder {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    @Test
    fun `User click add to cart non variant`() {
        val searchProductModel: SearchProductModel = inspirationListAtc.jsonToObject()

        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)
        `Given Load Data`()

        `When user click add to cart non variant`()

        `Then verify add to cart API will hit`()
    }

    @Test
    fun `User click add to cart with variant`() {
        val searchProductModel: SearchProductModel = inspirationListAtc.jsonToObject()

        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)
        `Given Load Data`()

        `When user click add to cart with variant`()

        `Then verify variant bottomsheet will show`()
    }

    private fun `When user click add to cart non variant`() {
        inspirationListAtcPresenterDelegate.onListAtcItemAddToCart(
            InspirationCarouselDataView.Option.Product(),
            ""
        )
    }

    private fun `When user click add to cart with variant`() {
        inspirationListAtcPresenterDelegate.onListAtcItemAddToCart(
            InspirationCarouselDataView.Option.Product(
                parentId = "123123"
            ),
            ""
        )
    }

    private fun `Then verify add to cart API will hit`() {
        verify {
            addToCartUseCase.execute(any(), any())
        }
    }

    private fun `Then verify variant bottomsheet will show`() {
        verify {
            inspirationListAtcView.openVariantBottomSheet(any(), any())
            inspirationListAtcView.trackAddToCartVariant(any())
        }
    }

    @Test
    fun `User click add to cart non variant and success`() {
        val searchProductModel: SearchProductModel = inspirationListAtc.jsonToObject()

        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)
        `Given Load Data`()
        `Given Add to cart is succeed`()

        `When user click add to cart non variant`()

        `Then verify searchbar notification updated`()
        `Then verify toaster has opened`()
        `Then verify item click and add to card has been fired`()
    }

    private fun `Given Add to cart is succeed`() {
        every { addToCartUseCase.execute(any(), any()) }.answers {
            firstArg<(AddToCartDataModel?) -> Unit>().invoke(AddToCartDataModel())
        }
    }

    private fun `Then verify searchbar notification updated`() {
        verify {
            inspirationListAtcView.updateSearchBarNotification()
        }
    }

    private fun `Then verify toaster has opened`() {
        verify {
            inspirationListAtcView.openAddToCartToaster(any())
        }
    }

    private fun `Then verify item click and add to card has been fired`() {
        verify {
            inspirationListAtcView.trackItemClick(any())
            inspirationListAtcView.trackAddToCart(any())
        }
    }

    private fun `Then verify visitable list has correct list atc and product sequence on first page`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured

        visitableList.size shouldBe 16
        // 0 -> choose address data
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> product
        // 5 -> inspiration list atc
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
        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                0 -> {
                    visitable.shouldBeInstanceOf<ChooseAddressDataView>(
                        "visitable list at index $index should be ChooseAddressDataViewModel"
                    )
                }
                5 -> {
                    visitable.shouldBeInstanceOf<InspirationListAtcDataView>(
                        "visitable list at index $index should be InspirationListAtcDataView"
                    )
                    (visitable as InspirationListAtcDataView).assertInspirationListAtcDataView(
                        searchProductModel.searchInspirationCarousel.data[0]
                    )
                }
                else -> {
                    visitable.shouldBeInstanceOf<ProductItemDataView>(
                        "visitable list at index $index should be ProductItemViewModel"
                    )
                }
            }
        }
    }

    private fun InspirationListAtcDataView.assertInspirationListAtcDataView(
        expectedData: SearchProductModel.InspirationCarouselData
    ) {
        this.type shouldBe expectedData.type

        val option = this.option
        val expectedOption = expectedData.inspirationCarouselOptions.first()
        option.layout shouldBe expectedData.layout
        option.position shouldBe expectedData.position
        option.title shouldBe expectedOption.title
        option.trackingOption shouldBe expectedData.trackingOption.toInt()
        option.url shouldBe expectedOption.url
        option.applink shouldBe expectedOption.applink
        option.bannerImageUrl shouldBe expectedOption.bannerImageUrl
        option.bannerLinkUrl shouldBe expectedOption.bannerLinkUrl
        option.bannerApplinkUrl shouldBe expectedOption.bannerApplinkUrl
        option.componentId shouldBe expectedOption.componentId
        option.dimension90 shouldBe expectedDimension90
        option.product.assert(
            expectedOption.inspirationCarouselProducts,
            expectedData.title,
            expectedData.type,
            expectedData.layout,
            1,
            expectedOption.title,
            expectedDimension90,
        )
    }
}
