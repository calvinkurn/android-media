package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.topads.sdk.domain.model.Data
import io.mockk.*
import org.junit.Before
import org.junit.Test
import rx.Subscriber

internal class SearchProductHandleVisitShopTest : ProductListPresenterTestFixtures() {

    private val searchProductModel = "searchproduct/with-topads.json".jsonToObject<SearchProductModel>()
    private val className = "SearchProductClassName"
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }
    private val goToShopPageDataLayerSlot = slot<Any>()
    private val goToShopPageDataLayer by lazy { goToShopPageDataLayerSlot.captured as Map<String, Any> }

    @Before
    fun setUpAddToCartTest() {
        `Given view already load data`()
    }

    private fun `Given view already load data`() {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        every { productListView.className } returns className

        productListPresenter.loadData(mapOf())
    }

    @Test
    fun `Handle visit shop action for top ads product`() {
        val topAdsData = searchProductModel.topAdsModel.data[0]
        val shopId = topAdsData.shop.id
        val productItemViewModel = visitableList.findProductItemWith(topAdsData.product.id, true)

        `Given view click three dots`(productItemViewModel)

        `When handle visit shop`()

        `Then verify top ads url hitter for click shop`(topAdsData)
        `Then verify view handle route to shop page`(shopId)

        val name = "/searchproduct - organic ads"
        val shopName = topAdsData.shop.name
        val shopUrl = topAdsData.shop.uri
        val position = productItemViewModel.position.toString()
        val categoryBreadcrumb = topAdsData.product.categoryBreadcrumb

        `Then assert go to shop page data layer`(shopId, name, shopName, shopUrl, position, categoryBreadcrumb)
    }

    private fun List<Visitable<*>>.findProductItemWith(productId: String, isAds: Boolean): ProductItemDataView {
        return find {
            it is ProductItemDataView && it.productID == productId && it.isAds == isAds
        } as ProductItemDataView
    }

    private fun `Given view click three dots`(productItemDataView: ProductItemDataView) {
        productListPresenter.onThreeDotsClick(productItemDataView, 0)
    }

    private fun `When handle visit shop`() {
        productListPresenter.handleVisitShopAction()
    }

    private fun `Then verify top ads url hitter for click shop`(topAdsData: Data) {
        verify {
            topAdsUrlHitter.hitClickUrl(
                    className,
                    topAdsData.shopClickUrl,
                    topAdsData.product.id,
                    topAdsData.product.name,
                    topAdsData.product.image.s_ecs,
                    SearchConstant.TopAdsComponent.TOP_ADS
            )
        }
    }

    private fun `Then verify view handle route to shop page`(shopId: String) {
        verify {
            productListView.routeToShopPage(shopId)
            productListView.trackEventGoToShopPage(capture(goToShopPageDataLayerSlot))
        }
    }

    private fun `Then assert go to shop page data layer`(
            shopId: String,
            name: String,
            shopName: String,
            shopUrl: String,
            position: String,
            categoryBreadcrumb: String
    ) {
        goToShopPageDataLayer["id"] shouldBe shopId
        goToShopPageDataLayer["name"] shouldBe name
        goToShopPageDataLayer["creative"] shouldBe shopName
        goToShopPageDataLayer["creative_url"] shouldBe shopUrl
        goToShopPageDataLayer["position"] shouldBe position
        goToShopPageDataLayer["category"] shouldBe categoryBreadcrumb
        goToShopPageDataLayer["promo_id"] shouldBe "none / other"
        goToShopPageDataLayer["promo_code"] shouldBe "none / other"
    }

    @Test
    fun `Handle visit shop action for organic product`() {
        val productItem = searchProductModel.searchProduct.data.productList[0]
        val shopId = productItem.shop.id
        val productItemViewModel = visitableList.findProductItemWith(productItem.id, false)

        `Given view click three dots`(productItemViewModel)

        `When handle visit shop`()

        `Then verify top ads url hitter is not called`()
        `Then verify view handle route to shop page`(shopId)

        val name = "/searchproduct - organic"
        val shopName = productItem.shop.name
        val shopUrl = productItem.shop.url
        val position = productItemViewModel.position.toString()
        val categoryBreadcrumb = productItem.categoryBreadcrumb

        `Then assert go to shop page data layer`(shopId, name, shopName, shopUrl, position, categoryBreadcrumb)
    }

    private fun `Then verify top ads url hitter is not called`() {
        verify(exactly = 0) {
            topAdsUrlHitter.hitClickUrl(any<String>(), any(), any(), any(), any(), any())
        }
    }
}