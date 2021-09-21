package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.search.utils.safeCastRupiahToInt
import io.mockk.*
import org.junit.Before
import org.junit.Test
import rx.Subscriber

internal class SearchProductHandleAddToCartTest : ProductListPresenterTestFixtures() {

    private val searchProductModel = "searchproduct/with-topads.json".jsonToObject<SearchProductModel>()
    private val cartId = "12345"
    private val className = "SearchProductClassName"
    private val errorMessageFromATC = "Error message from ATC"
    private val productCardOptionsModelSlot = slot<ProductCardOptionsModel>()
    private val productCardOptionModel by lazy { productCardOptionsModelSlot.captured }
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

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
    fun `Test add to cart tracking data layer`() {
        `Test ATC Data Layer official store`()
        `Test ATC Data Layer Power Merchant`()
        `Test ATC Data Layer Reguler`()
        `Test ATC Data Layer Top Ads`()
    }

    private fun `Test ATC Data Layer official store`() {
        val productItem = searchProductModel.searchProduct.data.productList.find { it.shop.isOfficial }!!
        val productItemViewModel = visitableList.findProductItemWith(productId = productItem.id, isAds = false)

        val addToCartDataLayer = productItemViewModel.getProductAsATCObjectDataLayer(cartId) as Map<String, Any>
        addToCartDataLayer.verify(productItem)
        addToCartDataLayer["shop_type"] shouldBe "official_store"
    }

    private fun `Test ATC Data Layer Power Merchant`() {
        val productItem = searchProductModel.searchProduct.data.productList.find { !it.shop.isOfficial && it.shop.isPowerBadge }!!
        val productItemViewModel = visitableList.findProductItemWith(productId = productItem.id, isAds = false)

        val addToCartDataLayer = productItemViewModel.getProductAsATCObjectDataLayer(cartId) as Map<String, Any>
        addToCartDataLayer.verify(productItem)
        addToCartDataLayer["shop_type"] shouldBe "gold_merchant"
    }

    private fun `Test ATC Data Layer Reguler`() {
        val productItem = searchProductModel.searchProduct.data.productList.find { !it.shop.isOfficial && !it.shop.isPowerBadge }!!
        val productItemViewModel = visitableList.findProductItemWith(productId = productItem.id, isAds = false)

        val addToCartDataLayer = productItemViewModel.getProductAsATCObjectDataLayer(cartId) as Map<String, Any>
        addToCartDataLayer.verify(productItem)
        addToCartDataLayer["shop_type"] shouldBe "reguler"
    }

    private fun List<Visitable<*>>.findProductItemWith(productId: String, isAds: Boolean): ProductItemDataView {
        return find {
            it is ProductItemDataView && it.productID == productId && it.isAds == isAds
        } as ProductItemDataView
    }

    private fun Map<String, Any>.verify(productItem: SearchProductModel.Product) {
        this["name"] shouldBe productItem.name
        this["id"] shouldBe productItem.id
        this["price"] shouldBe safeCastRupiahToInt(productItem.price).toString()
        this["brand"] shouldBe "none / other"
        this["category"] shouldBe productItem.categoryBreadcrumb
        this["variant"] shouldBe "none / other"
        this["quantity"] shouldBe productItem.minOrder
        this["shop_id"] shouldBe productItem.shop.id
        this["shop_name"] shouldBe productItem.shop.name
        this["category_id"] shouldBe productItem.categoryId
        this["dimension82"] shouldBe cartId
    }

    private fun `Test ATC Data Layer Top Ads`() {
        val topAdsData = searchProductModel.topAdsModel.data.find { it.shop.isShop_is_official }!!
        val productItemViewModel = visitableList.findProductItemWith(productId = topAdsData.product.id, isAds = true)

        val addToCartDataLayer = productItemViewModel.getProductAsATCObjectDataLayer(cartId) as Map<String, Any>

        addToCartDataLayer["name"] shouldBe topAdsData.product.name
        addToCartDataLayer["id"] shouldBe topAdsData.product.id
        addToCartDataLayer["price"] shouldBe safeCastRupiahToInt(topAdsData.product.priceFormat).toString()
        addToCartDataLayer["brand"] shouldBe "none / other"
        addToCartDataLayer["category"] shouldBe topAdsData.product.categoryBreadcrumb
        addToCartDataLayer["variant"] shouldBe "none / other"
        addToCartDataLayer["quantity"] shouldBe topAdsData.product.productMinimumOrder
        addToCartDataLayer["shop_id"] shouldBe topAdsData.shop.id
        addToCartDataLayer["shop_name"] shouldBe topAdsData.shop.name
        addToCartDataLayer["category_id"] shouldBe topAdsData.product.category.id
        addToCartDataLayer["dimension82"] shouldBe cartId
        addToCartDataLayer["shop_type"] shouldBe "official_store"
    }

    @Test
    fun `Handle add to cart for non login user`() {
        val indexedProductItem = visitableList.getIndexedProductItem(false)
        val productItemViewModel = indexedProductItem.value as ProductItemDataView
        val position = indexedProductItem.index

        `Given view click three dots`(productItemViewModel, position)
        `Given product card option model with add to cart not logged in`()

        `When handle add to cart action`()

        `Then verify view launch login page`()
    }

    private fun `Given view click three dots`(productItemDataView: ProductItemDataView, position: Int) {
        every { productListView.showProductCardOptions(capture(productCardOptionsModelSlot)) } just runs

        productListPresenter.onThreeDotsClick(productItemDataView, position)
    }

    private fun <T> List<T>.getIndexedProductItem(isAds: Boolean): IndexedValue<T> {
        return withIndex().find {
            it.value is ProductItemDataView && ((it.value as ProductItemDataView).isAds == isAds)
        }!!
    }

    private fun `Given product card option model with add to cart not logged in`() {
        productCardOptionModel.addToCartResult = ProductCardOptionsModel.AddToCartResult(isUserLoggedIn = false)
    }

    private fun `When handle add to cart action`() {
        productListPresenter.handleAddToCartAction(productCardOptionModel)
    }

    private fun `Then verify view launch login page`() {
        verify {
            productListView.launchLoginActivity("")
        }

        verify (exactly = 0) {
            productListView.showAddToCartFailedMessage(any())
            productListView.trackSuccessAddToCartEvent(any(), any())
            productListView.showAddToCartSuccessMessage()
            topAdsUrlHitter.hitClickUrl(any<String>(), any(), any(), any(), any(), any())
        }
    }

    @Test
    fun `Handle add to cart success for organic product`() {
        val indexedProductItem = visitableList.getIndexedProductItem(false)
        val productItemViewModel = indexedProductItem.value as ProductItemDataView
        val position = indexedProductItem.index

        `Given view click three dots`(productItemViewModel, position)
        `Given product card option model with add to cart success`()

        `When handle add to cart action`()

        `Then verify add to cart success view interaction for organic product`(productItemViewModel)
    }

    private fun `Then verify add to cart success view interaction for organic product`(productItemDataView: ProductItemDataView) {
        verify {
            productListView.trackSuccessAddToCartEvent(
                    false,
                    productItemDataView.getProductAsATCObjectDataLayer(cartId)
            )
            productListView.showAddToCartSuccessMessage()
        }

        verify (exactly = 0) {
            topAdsUrlHitter.hitClickUrl(any<String>(), any(), any(), any(), any(), any())
            productListView.launchLoginActivity(any())
            productListView.showAddToCartFailedMessage(any())
        }
    }

    private fun `Given product card option model with add to cart success`() {
        productCardOptionModel.addToCartResult = ProductCardOptionsModel.AddToCartResult(
                isUserLoggedIn = true,
                isSuccess = true,
                cartId = cartId
        )
    }

    @Test
    fun `Handle add to cart success for top ads product`() {
        val indexedProductItem = visitableList.getIndexedProductItem(true)
        val productItemViewModel = indexedProductItem.value as ProductItemDataView
        val position = indexedProductItem.index

        `Given view click three dots`(productItemViewModel, position)
        `Given product card option model with add to cart success`()

        `When handle add to cart action`()

        `Then verify add to cart success view interaction for top ads product`(productItemViewModel)
    }

    private fun `Then verify add to cart success view interaction for top ads product`(productItemDataView: ProductItemDataView) {
        verify {
            productListView.trackSuccessAddToCartEvent(
                    true,
                    productItemDataView.getProductAsATCObjectDataLayer(cartId)
            )
            productListView.showAddToCartSuccessMessage()
            topAdsUrlHitter.hitClickUrl(
                    className,
                    productItemDataView.topadsClickUrl,
                    productItemDataView.productID,
                    productItemDataView.productName,
                    productItemDataView.imageUrl,
                    SearchConstant.TopAdsComponent.TOP_ADS
            )
        }

        verify (exactly = 0) {
            productListView.launchLoginActivity(any())
            productListView.showAddToCartFailedMessage(any())
        }
    }

    @Test
    fun `Handle add to cart failed`() {
        val indexedProductItem = visitableList.getIndexedProductItem(true)
        val productItemViewModel = indexedProductItem.value as ProductItemDataView
        val position = indexedProductItem.index

        `Given view click three dots`(productItemViewModel, position)
        `Given product card model with add to cart fail`()

        `When handle add to cart action`()

        `Then verify add to cart fail view interaction`()
    }

    private fun `Given product card model with add to cart fail`() {
        productCardOptionModel.addToCartResult = ProductCardOptionsModel.AddToCartResult(
                isUserLoggedIn = true,
                isSuccess = false,
                errorMessage = errorMessageFromATC
        )
    }

    private fun `Then verify add to cart fail view interaction`() {
        verify {
            productListView.showAddToCartFailedMessage(errorMessageFromATC)
        }

        verify(exactly = 0) {
            productListView.launchLoginActivity(any())
            productListView.trackSuccessAddToCartEvent(any(), any())
            productListView.showAddToCartSuccessMessage()
            topAdsUrlHitter.hitClickUrl(any<String>(), any(), any(), any(), any(), any())
        }
    }
}