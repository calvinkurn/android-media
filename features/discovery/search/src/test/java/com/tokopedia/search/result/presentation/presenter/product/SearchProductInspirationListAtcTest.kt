package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTracking
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcDataView
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
    private val clickedProductTest = InspirationCarouselDataView.Option.Product(
        id = "3747018003",
        name = "EDIFIER R12U - Active USB Powered Speakers Desktop/Laptop audio Black",
    )

    private val clickedProductWithVariantTest = InspirationCarouselDataView.Option.Product(
        id = "2773621125",
        name = "Edifier R1080BT Wireless Bluetooth Small Speaker Subwoofer 2.0 Wooden",
        parentId = "123123",
    )

    private val clickedProductAdsTest = InspirationCarouselDataView.Option.Product(
        id = "3747018003",
        name = "EDIFIER R12U - Active USB Powered Speakers Desktop/Laptop audio Black",
        isOrganicAds = true,
    )

    private val expectedType = "same_shop"

    private val expectedAtcSuccessResponse = AddToCartDataModel(
        data = DataModel(
            cartId = "123123",
            quantity = 1,
            message = arrayListOf("1 Barang berhasil di tambahkan ke keranjang")
        )
    )

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
        productListPresenter.onListAtcItemAddToCart(
            clickedProductTest,
            expectedType
        )
    }

    private fun `When user click add to cart non variant product ads`() {
        productListPresenter.onListAtcItemAddToCart(
            clickedProductAdsTest,
            expectedType
        )
    }

    private fun `When user click add to cart with variant`() {
        productListPresenter.onListAtcItemAddToCart(
            clickedProductWithVariantTest,
            expectedType
        )
    }

    private fun `Then verify add to cart API will hit`() {
        verify {
            addToCartUseCase.execute(any(), any())
        }
    }

    private fun `Then verify variant bottomsheet will show`() {
        verify {
            inspirationListAtcView.openVariantBottomSheet(clickedProductWithVariantTest, expectedType)
            inspirationListAtcView.trackAddToCartVariant(clickedProductWithVariantTest)
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
        `Then verify item click and add to card has been tracked`(clickedProductTest)
    }

    @Test
    fun `User click add to cart non variant product ads and success`() {
        val searchProductModel: SearchProductModel = inspirationListAtc.jsonToObject()

        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)
        `Given Load Data`()
        `Given Add to cart is succeed`()

        `When user click add to cart non variant product ads`()

        `Then verify searchbar notification updated`()
        `Then verify toaster has opened`()
        `Then verify item click and add to card has been tracked`(clickedProductAdsTest)
    }

    private fun `Given Add to cart is succeed`() {
        every { addToCartUseCase.execute(any(), any()) }.answers {
            firstArg<(AddToCartDataModel?) -> Unit>().invoke(expectedAtcSuccessResponse)
        }
    }

    private fun `Then verify searchbar notification updated`() {
        verify {
            inspirationListAtcView.updateSearchBarNotification()
        }
    }

    private fun `Then verify toaster has opened`() {
        verify {
            inspirationListAtcView.openAddToCartToaster(
                expectedAtcSuccessResponse.data.message.first(),
                true,
            )
        }
    }

    private fun `Then verify item click and add to card has been tracked`(
        expectedProduct: InspirationCarouselDataView.Option.Product
    ) {
        val expectedTrackData = InspirationCarouselTracking.Data(
            "",
            expectedProduct,
            "",
            expectedAtcSuccessResponse.data.cartId,
            expectedAtcSuccessResponse.data.quantity,
        )

        verify {
            inspirationListAtcView.trackItemClick(expectedTrackData)
            inspirationListAtcView.trackAddToCart(expectedTrackData)
        }
    }

    private fun `Then Verify TopAds Click Url Hit`() {
        verify {
            inspirationListAtcView.trackAdsClick(clickedProductAdsTest)
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
                }
                else -> {
                    visitable.shouldBeInstanceOf<ProductItemDataView>(
                        "visitable list at index $index should be ProductItemViewModel"
                    )
                }
            }
        }
    }
}
