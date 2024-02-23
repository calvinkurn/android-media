package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val searchProductWithTopAdsResponseJSON = "searchproduct/with-topads.json"

internal class SearchProductHandleClickProductTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val adapterPosition = 1
    private val userId = "12345678"
    private val capturedProductItemViewModel = slot<ProductItemDataView>()

    @Test
    fun `Handle onProductClick with null ProductItemViewModel`() {
        `When handle product click`(null)

        `Then verify view not doing anything`()
    }

    private fun `When handle product click`(productItemDataView: ProductItemDataView?) {
        productListPresenter.onProductClick(productItemDataView, adapterPosition)
    }

    private fun `Then verify view not doing anything`() {
        confirmVerified(productListView)
    }

    @Test
    fun `Handle onProductClick for Top Ads Product`() {
        `Given View already load data`(searchProductWithTopAdsResponseJSON)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = true, isOrganicAds = false)

        `When handle product click`(productItemViewModel)

        `Then verify view interaction for Top Ads Product`(productItemViewModel)
        `Then verify position is correct`(productItemViewModel)
    }

    private fun `Given View already load data`(responseJSON: String) {
        val searchProductModel = responseJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given className from view`()
        `Given view already load data`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given className from view`() {
        every { productListView.className } returns className
    }

    private fun `Given view already load data`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }
        productListPresenter.loadData(searchParameter)
    }

    private fun findProductItemFromVisitableList(isTopAds: Boolean = false, isOrganicAds: Boolean = false): ProductItemDataView {
        val visitableList = visitableListSlot.captured

        return visitableList.find { it is ProductItemDataView && it.isTopAds == isTopAds && it.isOrganicAds == isOrganicAds } as ProductItemDataView
    }

    private fun `Then verify view interaction for Top Ads Product`(productItemDataView: ProductItemDataView) {
        verify {
            productListView.className

            topAdsUrlHitter.hitClickUrl(
                    className,
                    productItemDataView.topadsClickUrl,
                    productItemDataView.productID,
                    productItemDataView.productName,
                    productItemDataView.imageUrl,
                    SearchConstant.TopAdsComponent.TOP_ADS
            )

            productListView.sendTopAdsGTMTrackingProductClick(capture(capturedProductItemViewModel))
            productListView.routeToProductDetail(productItemDataView, adapterPosition)
        }
    }

    private fun `Then verify position is correct`(productItemDataView: ProductItemDataView) {
        val product = capturedProductItemViewModel.captured
        assert(product.position == productItemDataView.position)
    }

    @Test
    fun `Handle onProductClick for non Top Ads Product`() {
        `Given View already load data`(searchProductWithTopAdsResponseJSON)

        `Given user session data`()

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = false, isOrganicAds = false)

        `When handle product click`(productItemViewModel)

        `Then verify view interaction is correct for non Top Ads Product`(productItemViewModel)
    }

    private fun `Given user session data`() {
        every { userSession.isLoggedIn } returns true
        every { userSession.userId } returns userId
    }

    private fun `Then verify view interaction is correct for non Top Ads Product`(productItemDataView: ProductItemDataView) {
        verify {
            productListView.sendGTMTrackingProductClick(productItemDataView, userId, any())
            productListView.sendByteIOTrackingProductClick(productItemDataView)
            productListView.routeToProductDetail(productItemDataView, adapterPosition)
        }
    }

    @Test
    fun `Handle onProductClick for organic ads product`() {
        `Given View already load data`(searchProductWithTopAdsResponseJSON)

        `Given user session data`()

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = false, isOrganicAds = true)

        `When handle product click`(productItemViewModel)

        `Then verify view interaction is correct for organic Ads Product`(productItemViewModel)
    }

    private fun `Then verify view interaction is correct for organic Ads Product`(productItemDataView: ProductItemDataView) {
        verify {
            productListView.className

            topAdsUrlHitter.hitClickUrl(
                    className,
                    productItemDataView.topadsClickUrl,
                    productItemDataView.productID,
                    productItemDataView.productName,
                    productItemDataView.imageUrl,
                    SearchConstant.TopAdsComponent.ORGANIC_ADS
            )

            productListView.sendGTMTrackingProductClick(productItemDataView, userId, any())
            productListView.sendByteIOTrackingProductClick(productItemDataView)
            productListView.routeToProductDetail(productItemDataView, adapterPosition)
        }
    }

    @Test
    fun `Handle onProductClick for organic product with related keyword`() {
        val searchProductModel = "searchproduct/impressionclick/related.json".jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given className from view`()
        `Given view already load data`()
        `Given user session data`()

        val productItem = findProductItemFromVisitableList(
            isTopAds = false,
            isOrganicAds = false
        )

        `When handle product click`(productItem)

        verify {
            productListView.sendGTMTrackingProductClick(
                productItem,
                userId,
                searchProductModel.searchProduct.relatedKeyword,
            )
            productListView.sendByteIOTrackingProductClick(productItem)
        }
    }

    @Test
    fun `Handle onProductClick for organic product with suggested keyword`() {
        val searchProductModel = "searchproduct/impressionclick/suggestion.json"
            .jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given className from view`()
        `Given view already load data`()
        `Given user session data`()

        val productItem = findProductItemFromVisitableList(
            isTopAds = false,
            isOrganicAds = false
        )

        `When handle product click`(productItem)

        verify {
            productListView.sendGTMTrackingProductClick(
                productItem,
                userId,
                searchProductModel.searchProduct.data.suggestion.suggestion,
            )
            productListView.sendByteIOTrackingProductClick(productItem)
        }
    }

    @Test
    fun `Handle onProductClick for organic product with related keyword for reimagine`() {
        val searchProductModel = "searchproduct/impressionclick/related-reimagine.json".jsonToObject<SearchProductModel>()
        `Given search reimagine rollence product card will return non control variant`()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given className from view`()
        `Given view already load data`()
        `Given user session data`()

        val productItem = findProductItemFromVisitableList(
            isTopAds = false,
            isOrganicAds = false
        )

        `When handle product click`(productItem)

        verify {
            productListView.sendGTMTrackingProductClick(
                productItem,
                userId,
                searchProductModel.searchProductV5.relatedKeyword,
            )
            productListView.sendByteIOTrackingProductClick(productItem)
        }
    }

    @Test
    fun `Handle onProductClick for organic product with suggested keyword for reimagine`() {
        val searchProductModel = "searchproduct/impressionclick/suggestion-reimagine.json".jsonToObject<SearchProductModel>()
        `Given search reimagine rollence product card will return non control variant`()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given className from view`()
        `Given view already load data`()
        `Given user session data`()

        val productItem = findProductItemFromVisitableList(
            isTopAds = false,
            isOrganicAds = false
        )

        `When handle product click`(productItem)

        verify {
            productListView.sendGTMTrackingProductClick(
                productItem,
                userId,
                searchProductModel.searchProductV5.data.suggestion.suggestion,
            )
            productListView.sendByteIOTrackingProductClick(productItem)
        }
    }
}
