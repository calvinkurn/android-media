package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.MINUS_IDS
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.POST_ATC_CATEGORY_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.POST_ATC_PRODUCT_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.POST_ATC_SHOP_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.POST_ATC_WAREHOUSE_ID
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationCarousel
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.ByteIOTrackingData
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTracking
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcDataView
import com.tokopedia.search.result.product.inspirationlistatc.postatccarousel.InspirationListPostAtcDataView
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test
import rx.Subscriber

private const val inspirationListAtc = "searchproduct/inspirationlistatc/inspiration-list-atc.json"
private const val inspirationListAtcWithCategoryId = "searchproduct/inspirationlistpostatc/inspiration-list-atc-with-category-id.json"
private const val inspirationListPostAtc = "searchproduct/inspirationlistpostatc/inspiration-list-post-atc.json"

internal class SearchProductInspirationListAtcTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }
    private val keyword = "samsung"
    private val searchParameter = mapOf<String, Any>(
        SearchApiConst.Q to keyword,
        SearchApiConst.START to "0",
        SearchApiConst.UNIQUE_ID to "unique_id",
        SearchApiConst.USER_ID to "0",
    )
    private val atcDataViewFirstIndex = 0
    private val expectedType = "same_shop"
    private val expectedTypePostAtc = "post_atc"
    private val expectedAtcSuccessResponse = AddToCartDataModel(
        data = DataModel(
            cartId = "123123",
            quantity = 1,
            message = arrayListOf("1 Barang berhasil di tambahkan ke keranjang")
        )
    )
    private val postAtcSlot = slot<InspirationListPostAtcDataView>()
    private val selectedAtcSlot = slot<Visitable<*>>()
    private val requestParamsSlot = slot<RequestParams>()
    private val indexPostAtcSlot = slot<Int>()

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

    private fun `Given visitable list will be captured`() {
        every {
            productListView.setProductList(capture(visitableListSlot))
        } just runs
    }

    @Test
    fun `User click add to cart non variant`() {
        val searchProductModel: SearchProductModel = inspirationListAtc.jsonToObject()

        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)
        `Given visitable list will be captured`()
        `Given Load Data`()

        val clickedProductAtc =
            visitableList.filterIsInstance<InspirationListAtcDataView>()[atcDataViewFirstIndex]
                .option.product[0]
        `When user click add to cart`(clickedProductAtc)

        `Then verify add to cart API will hit`()
    }

    private fun `When user click add to cart`(
        clickedProduct: InspirationCarouselDataView.Option.Product,
        inspirationAtcType : String= expectedType,
    ) {
        productListPresenter.onListAtcItemAddToCart(
            clickedProduct,
            inspirationAtcType
        )
    }

    private fun `Then verify add to cart API will hit`() {
        verify {
            addToCartUseCase.execute(any(), any())
        }
    }

    @Test
    fun `User click add to cart with variant`() {
        val searchProductModel: SearchProductModel = inspirationListAtc.jsonToObject()

        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)
        `Given visitable list will be captured`()
        `Given Load Data`()

        val clickedProductAtc =
            visitableList.filterIsInstance<InspirationListAtcDataView>()[atcDataViewFirstIndex]
                .option.product[1]
        `When user click add to cart`(clickedProductAtc)

        `Then verify variant bottomsheet will show`(clickedProductAtc)
        `Then Verify Track Add To Cart Variant`(clickedProductAtc)
    }

    private fun `Then verify variant bottomsheet will show`(clickedProduct: InspirationCarouselDataView.Option.Product) {
        verify {
            inspirationListAtcView.openVariantBottomSheet(clickedProduct, expectedType, any())
            inspirationListAtcView.trackAddToCartVariant(clickedProduct)
        }
    }

    @Test
    fun `User click add to cart non variant and success`() {
        val searchProductModel: SearchProductModel = inspirationListAtc.jsonToObject()

        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)
        `Given visitable list will be captured`()
        `Given Load Data`()
        `Given Add to cart is succeed`()

        val clickedProductAtc =
            visitableList.filterIsInstance<InspirationListAtcDataView>()[atcDataViewFirstIndex]
                .option.product[0]
        `When user click add to cart`(clickedProductAtc)

        `Then verify searchbar notification updated`()
        `Then verify toaster has opened`()
        `Then verify item click and add to card has been tracked`(clickedProductAtc)
    }

    @Test
    fun `User click add to cart non variant product ads and success`() {
        val searchProductModel: SearchProductModel = inspirationListAtc.jsonToObject()

        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)
        `Given visitable list will be captured`()
        `Given Load Data`()
        `Given Add to cart is succeed`()

        val clickedProductAtc =
            visitableList.filterIsInstance<InspirationListAtcDataView>()[atcDataViewFirstIndex]
                .option.product[0]
        `When user click add to cart`(clickedProductAtc)

        `Then verify searchbar notification updated`()
        `Then verify toaster has opened`()
        `Then verify item click and add to card has been tracked`(clickedProductAtc)
        `Then Verify TopAds Click Url Hit`(clickedProductAtc)
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

    private fun `Then Verify TopAds Click Url Hit`(clickedProduct: InspirationCarouselDataView.Option.Product) {
        verify {
            inspirationListAtcView.trackAdsClick(clickedProduct)
        }
    }

    private fun `Then Verify Track Add To Cart Variant`(clickedProduct: InspirationCarouselDataView.Option.Product) {
        verify {
            inspirationListAtcView.trackAddToCartVariant(clickedProduct)
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

    @Test
    fun `Show inspiration list post atc carousel`() {
        val searchProductModel: SearchProductModel = inspirationListAtcWithCategoryId.jsonToObject()
        val inspirationListPostAtc: SearchInspirationCarousel = inspirationListPostAtc.jsonToObject()

        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel)
        `Given visitable list will be captured`()
        `Given Load Data`()
        `Given viewUpdater itemList return visitableList`()
        `Given Add to cart is succeed`()

        `Given Inspiration Post Atc API will return Inspiration Post Atc Model`(inspirationListPostAtc)
        `Given recyclerViewUpdater post atc list captured`()

        val inspirationListAtc = visitableList.filterIsInstance<InspirationListAtcDataView>()[atcDataViewFirstIndex]
        val clickedProductAtc =
            inspirationListAtc.option.product[0]
        `When user click add to cart`(clickedProductAtc)

        `Then verify get post atc carousel is executed`(clickedProductAtc)
        `Then verify recyclerview update`()
    }

    @Test
    fun `Replace inspiration list post atc carousel`() {
        val inspirationListPostAtc: SearchInspirationCarousel = inspirationListPostAtc.jsonToObject()

        `Given Dummy Post Atc in List Visitable`()
        `Given Inspiration Post Atc API will return Inspiration Post Atc Model`(inspirationListPostAtc)
        `Given recyclerView refresh post atc list captured`()
        `Given Add to cart is succeed`()

        val inspirationListAtc = `Create List Dummy Atc Carousel`() as InspirationListAtcDataView
        val clickedProductAtc =
            inspirationListAtc.option.product[0]
        `When user click add to cart`(clickedProductAtc)

        `Then verify get post atc carousel is executed`(clickedProductAtc)
        `Then verify recyclerview is refresh on InspirationListPostAtcDataView`()
    }

    @Test
    fun `Hide inspiration list post atc carousel`() {
        val selectedClosePostAtc = `Create List Dummy Post Atc Carousel`() as InspirationListPostAtcDataView
        `Given Dummy Post Atc in List Visitable`()
        productListPresenter.setVisibilityInspirationCarouselPostAtcOnVisitableList(false, selectedClosePostAtc)
        verify {
            viewUpdater.refreshItemAtIndex(any(), any())
        }
    }

    @Test
    fun `Cancel Hide inspiration list post atc carousel`() {
        val selectedClosePostAtc = `Create List Dummy Post Atc Carousel`() as InspirationListPostAtcDataView
        `Given Dummy Post Atc in List Visitable`()
        productListPresenter.setVisibilityInspirationCarouselPostAtcOnVisitableList(true, selectedClosePostAtc)
        verify {
            viewUpdater.refreshItemAtIndex(any(), any())
        }
    }

    @Test
    fun `Inspiration list post atc click not hit API post atc carousel`() {
        val selectedClosePostAtc = `Create List Dummy Post Atc Carousel`() as InspirationListPostAtcDataView
        val clickedProductAtc =
            selectedClosePostAtc.option.product[0]
        `When user click add to cart`(clickedProductAtc, expectedTypePostAtc)
        `Then verify get post atc carousel is not executed`()
    }

    private fun `Given viewUpdater itemList return visitableList`() {
        every { viewUpdater.itemList } returns visitableList
    }

    private fun `Given recyclerViewUpdater post atc list captured`(){
        every {
            viewUpdater.insertItemAfter(
            capture(postAtcSlot),
            capture(selectedAtcSlot)
            )
        } just runs
    }

    private fun `Given recyclerView refresh post atc list captured`(){
        every {
            viewUpdater.refreshItemAtIndex(
                capture(indexPostAtcSlot),
                capture(selectedAtcSlot)
            )
        } just runs
    }

    private fun `Given Inspiration Post Atc API will return Inspiration Post Atc Model` (
        searchInspirationPostAtc : SearchInspirationCarousel,
    ) {
        every {
            getPostATCCarouselUseCase.execute(
                capture(requestParamsSlot),
                any()
            )
        }.answers {
            secondArg<Subscriber<SearchInspirationCarousel>>().complete(
                searchInspirationPostAtc
            )
        }
    }
    private fun `Then verify get post atc carousel is executed`(
        clickedProductAtc: InspirationCarouselDataView.Option.Product,
    ) {
        verify {
            getPostATCCarouselUseCase.execute(any(), any())
        }

        val parameters = requestParamsSlot.captured.parameters
        assertThat(parameters[POST_ATC_SHOP_ID], `is`(clickedProductAtc.shopId))
        assertThat(parameters[POST_ATC_CATEGORY_ID], `is`(clickedProductAtc.categoryID))
        assertThat(parameters[POST_ATC_PRODUCT_ID], `is`(clickedProductAtc.id))
        assertThat(parameters[POST_ATC_WAREHOUSE_ID], `is`(clickedProductAtc.warehouseID))
        assertThat(parameters[MINUS_IDS], `is`(getListDeduplication()))
    }

    private fun `Then verify get post atc carousel is not executed`() {
        verify(exactly = 0) {
            getPostATCCarouselUseCase.execute(any(), any())
        }
    }

    private fun `Then verify recyclerview update`() {
        verify {
            viewUpdater.insertItemAfter(postAtcSlot.captured, selectedAtcSlot.captured)
        }
    }

    private fun `Then verify recyclerview is refresh on InspirationListPostAtcDataView`() {
        verify {
            viewUpdater.refreshItemAtIndex(indexPostAtcSlot.captured, selectedAtcSlot.captured)
        }
    }

    private fun `Given Dummy Post Atc in List Visitable`() {
        val dummyProductItemDataView = ProductItemDataView()
        val listVisitable = arrayListOf<Visitable<*>>()
        for (i in 0..3) {
            listVisitable.add(dummyProductItemDataView)
        }
        listVisitable.add(`Create List Dummy Atc Carousel`())
        listVisitable.add(`Create List Dummy Post Atc Carousel`() ?: return)
        every { viewUpdater.itemList } returns listVisitable
    }

    private fun `Create List Dummy Post Atc Carousel`(): Visitable<*>? {
        val dummyInspirationListPostAtc: SearchInspirationCarousel =
            inspirationListPostAtc.jsonToObject()
        return InspirationListPostAtcDataView.InspirationListPostAtcDataViewMapper.convertToInspirationListPostAtcDataView(
            ByteIOTrackingData(),
            InspirationCarouselDataView.Option.Product(),
            dummyInspirationListPostAtc.data[0]
        )
    }

    private fun `Create List Dummy Atc Carousel`(): Visitable<*> {
        return InspirationListAtcDataView(
            option = InspirationCarouselDataView.Option(product = arrayListOf(
                InspirationCarouselDataView.Option.Product(id = "1234")
            ))
        )
    }
}
