package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val showButtonAtcModel = "searchproduct/showbuttonatc/common-response-with-show-button-atc-true.json"

internal class SearchProductShowButtonAtcClickTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }
    private val keyword = "samsung"
    private val searchParameter = mapOf<String, Any>(
        SearchApiConst.Q to keyword,
        SearchApiConst.START to "0",
        SearchApiConst.UNIQUE_ID to "unique_id",
        SearchApiConst.USER_ID to "0",
    )
    private lateinit var clickedProductAtc: ProductItemDataView

    private val expectedAtcSuccessResponse = AddToCartDataModel(
        data = DataModel(
            cartId = "123123",
            quantity = 1,
            message = arrayListOf("1 Barang berhasil di tambahkan ke keranjang")
        )
    )

    private val expectedFailedMessage = "Gagal"

    @Test
    fun `Click Add To Cart Non Variant And Success`() {
        val searchProductModel: SearchProductModel = showButtonAtcModel.jsonToObject()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given visitable list will be captured`()
        `Given Load Data`()
        `Given Add to cart is succeed`()

        `When User Click Non Variant Product ATC`()

        `Then verify add to cart API will hit`()
        `Then Verify ATC Notification Updated`()
        `Then Verify Toaster Opened`(
            expectedAtcSuccessResponse.data.message.firstOrNull() ?: "",
            true
        )
        `Then Verify Product Click Tracked`()
        `Then Verify Add To Cart Tracked`()
    }

    @Test
    fun `Click Add To Cart Non Variant And Failed`() {
        val searchProductModel: SearchProductModel = showButtonAtcModel.jsonToObject()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given visitable list will be captured`()
        `Given Load Data`()
        `Given Add to cart is failed`()

        `When User Click Non Variant Product ATC`()

        `Then verify add to cart API will hit`()
        `Then Verify Toaster Opened`(
            expectedFailedMessage,
            false
        )
    }

    @Test
    fun `Click Add To Cart Variant`() {
        val searchProductModel: SearchProductModel = showButtonAtcModel.jsonToObject()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given visitable list will be captured`()
        `Given Load Data`()
        `Given Add to cart is succeed`()

        `When User Click Variant Product ATC`()

        `Then Verify Variant BottomSheet Opened`()
    }

    @Test
    fun `Click Add To Cart Non Variant Ads And Success`() {
        val searchProductModel: SearchProductModel = showButtonAtcModel.jsonToObject()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given visitable list will be captured`()
        `Given Load Data`()
        `Given Add to cart is succeed`()

        `When User Click Non Variant Product Ads ATC`()

        `Then verify add to cart API will hit`()
        `Then Verify ATC Notification Updated`()
        `Then Verify Toaster Opened`(
            expectedAtcSuccessResponse.data.message.firstOrNull() ?: "",
            true
        )
        `Then Verify Product Click Tracked`()
        `Then Verify TopAds Url Hit`()
        `Then Verify TopAds GTM Click Tracked`()
        `Then Verify Add To Cart Tracked`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given Load Data`() {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Given visitable list will be captured`() {
        every {
            productListView.setProductList(capture(visitableListSlot))
        } just runs
    }

    private fun `When User Click Non Variant Product ATC`() {
        clickedProductAtc = visitableList.filterIsInstance<ProductItemDataView>()[0]
        productListPresenter.onProductAddToCart(clickedProductAtc)
    }

    private fun `When User Click Variant Product ATC`() {
        clickedProductAtc = visitableList.filterIsInstance<ProductItemDataView>()[1]
        productListPresenter.onProductAddToCart(clickedProductAtc)
    }

    private fun `When User Click Non Variant Product Ads ATC`() {
        clickedProductAtc = visitableList.filterIsInstance<ProductItemDataView>()[2]
        productListPresenter.onProductAddToCart(clickedProductAtc)
    }

    private fun `Then verify add to cart API will hit`() {
        verify {
            addToCartUseCase.execute(any(), any())
        }
    }

    private fun `Given Add to cart is succeed`() {
        every { addToCartUseCase.execute(any(), any()) }.answers {
            firstArg<(AddToCartDataModel?) -> Unit>().invoke(expectedAtcSuccessResponse)
        }
    }

    private fun `Given Add to cart is failed`() {
        every { addToCartUseCase.execute(any(), any()) }.answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable(expectedFailedMessage))
        }
    }

    private fun `Then Verify ATC Notification Updated`() {
        verify {
            productListView.updateSearchBarNotification()
        }
    }

    private fun `Then Verify Toaster Opened`(expectedMessage: String, expectedIsSuccess: Boolean) {
        verify {
            productListView.openAddToCartToaster(expectedMessage, expectedIsSuccess)
        }
    }

    private fun `Then Verify Product Click Tracked`() {
        verify {
            productListPresenter.trackProductClick(clickedProductAtc)
        }
    }

    private fun `Then Verify Add To Cart Tracked`() {
        verify {
            productListView.sendGTMTrackingProductATC(
                clickedProductAtc,
                expectedAtcSuccessResponse.data.cartId
            )
        }
    }

    private fun `Then Verify Variant BottomSheet Opened`() {
        verify {
            productListView.openVariantBottomSheet(clickedProductAtc)
        }
    }

    private fun `Then Verify TopAds Url Hit`() {
        verify {
            topAdsUrlHitter.hitClickUrl(
                productListView.className,
                clickedProductAtc.topadsClickUrl,
                clickedProductAtc.productID,
                clickedProductAtc.productName,
                clickedProductAtc.imageUrl,
                SearchConstant.TopAdsComponent.TOP_ADS
            )
        }
    }

    private fun `Then Verify TopAds GTM Click Tracked`() {
        verify {
            productListView.sendTopAdsGTMTrackingProductClick(clickedProductAtc)
        }
    }
}
