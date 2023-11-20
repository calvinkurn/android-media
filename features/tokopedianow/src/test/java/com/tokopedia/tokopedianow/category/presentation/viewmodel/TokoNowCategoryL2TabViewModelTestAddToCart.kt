package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2TabData
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.unit.test.ext.verifyValueEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TokoNowCategoryL2TabViewModelTestAddToCart: TokoNowCategoryL2TabViewModelTestFixture() {

    @Test
    fun `given product list when onSuccessGetMiniCartData should update product order quantity`() {
        onGetProductList(thenReturn = getProductResponse)
        onGetProductAds(thenReturn = getProductAdsResponse)

        val componentList = getCategoryLayoutResponse.components

        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            componentList = componentList
        )

        val miniCartItemProduct = MiniCartItem.MiniCartItemProduct(
            productId = "6903919751",
            quantity = 5
        )

        val miniCartItemProductAds = MiniCartItem.MiniCartItemProduct(
            productId = "7029764088",
            quantity = 3
        )

        val cartProductMap = MiniCartItemKey("6903919751") to miniCartItemProduct
        val cartProductAdsMap = MiniCartItemKey("7029764088") to miniCartItemProductAds

        val miniCartData = MiniCartSimplifiedData(
            miniCartItems = mapOf(cartProductMap, cartProductAdsMap)
        )

        viewModel.onViewCreated(data)
        viewModel.onSuccessGetMiniCartData(miniCartData)

        val visitableListLiveData = viewModel.visitableListLiveData.value.orEmpty()

        visitableListLiveData
            .verifyProductOrderQuantity(index = 2, expectedProductQuantity = 5)

        visitableListLiveData
            .verifyProductAdsOrderQuantity(index = 0, expectedProductQuantity = 3)
    }

    @Test
    fun `given product list when add to cart should call add to cart use case and update toolbar notification`() {
        runTest {
            val quantity = 3
            val layoutType = "product_card_item"

            onGetProductList(thenReturn = getProductResponse)
            onGetProductAds(thenReturn = getProductAdsResponse)
            onAddToCart_thenReturn(response = AddToCartDataModel())
            onGetIsUserLoggedIn_thenReturn(isLoggedIn = true)

            val componentList = getCategoryLayoutResponse.components

            val data = CategoryL2TabData(
                title = categoryTitle,
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = categoryIdL2,
                componentList = componentList
            )

            viewModel.onViewCreated(data)

            val productItem = viewModel.visitableListLiveData.value.orEmpty()
                .filterIsInstance<ProductItemDataView>()[2]

            val product = productItem.productCardModel
            val shopId = productItem.shopId

            viewModel.onCartQuantityChanged(product, shopId, quantity, layoutType)
            advanceTimeBy(1000L)

            val expectedTrackerModel = CategoryAtcTrackerModel(
                index = productItem.position,
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = categoryIdL2,
                quantity = quantity,
                shopId = productItem.shop.id,
                shopName = productItem.shop.name,
                shopType = productItem.shopType,
                categoryBreadcrumbs = productItem.categoryBreadcrumbs,
                product = productItem.productCardModel,
                layoutType = layoutType
            )

            verifyAddToCartUseCaseCalled()

            viewModel.atcDataTracker
                .verifyValueEquals(expectedTrackerModel)

            viewModel.updateToolbarNotification
                .verifyValueEquals(Unit)
        }
    }

    @Test
    fun `given product list when add to cart product ads should call add to cart use case and update toolbar notification`() {
        runTest {
            val quantity = 3
            val layoutType = "product_ads_carousel"

            onGetProductList(thenReturn = getProductResponse)
            onGetProductAds(thenReturn = getProductAdsResponse)
            onAddToCart_thenReturn(response = AddToCartDataModel())
            onGetIsUserLoggedIn_thenReturn(isLoggedIn = true)

            val componentList = getCategoryLayoutResponse.components

            val data = CategoryL2TabData(
                title = categoryTitle,
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = categoryIdL2,
                componentList = componentList
            )

            viewModel.onViewCreated(data)

            val productItem = viewModel.visitableListLiveData.value.orEmpty()
                .filterIsInstance<TokoNowAdsCarouselUiModel>().first().items[0]

            val product = productItem.productCardModel
            val shopId = productItem.shopId

            viewModel.onCartQuantityChanged(product, shopId, quantity, layoutType)
            advanceTimeBy(1000L)

            val expectedTrackerModel = CategoryAtcTrackerModel(
                index = productItem.position,
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = categoryIdL2,
                quantity = quantity,
                shopId = productItem.shopId,
                shopName = productItem.shopName,
                shopType = productItem.shopType,
                categoryBreadcrumbs = productItem.categoryBreadcrumbs,
                product = productItem.productCardModel,
                layoutType = layoutType
            )

            verifyAddToCartUseCaseCalled()

            viewModel.atcDataTracker
                .verifyValueEquals(expectedTrackerModel)

            viewModel.updateToolbarNotification
                .verifyValueEquals(Unit)
        }
    }

    @Test
    fun `given layout type is unknown when add to cart should not track add to cart data`() {
        runTest {
            val quantity = 3
            val layoutType = "some_unknown_type"

            onGetProductList(thenReturn = getProductResponse)
            onGetProductAds(thenReturn = getProductAdsResponse)
            onAddToCart_thenReturn(response = AddToCartDataModel())
            onGetIsUserLoggedIn_thenReturn(isLoggedIn = true)

            val componentList = getCategoryLayoutResponse.components

            val data = CategoryL2TabData(
                title = categoryTitle,
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = categoryIdL2,
                componentList = componentList
            )

            viewModel.onViewCreated(data)

            val productItem = viewModel.visitableListLiveData.value.orEmpty()
                .filterIsInstance<ProductItemDataView>()[2]

            val product = productItem.productCardModel
            val shopId = productItem.shopId

            viewModel.onCartQuantityChanged(product, shopId, quantity, layoutType)
            advanceTimeBy(1000L)

            viewModel.atcDataTracker
                .verifyValueEquals(null)
        }
    }

    @Test
    fun `given miniCartData when update cart item should call update cart use case and update toolbar notification`() {
        runTest {
            val quantity = 3
            val layoutType = "product_card_item"

            onGetProductList(thenReturn = getProductResponse)
            onGetProductAds(thenReturn = getProductAdsResponse)
            onUpdateCartItem_thenReturn(response = UpdateCartV2Data())
            onGetIsUserLoggedIn_thenReturn(isLoggedIn = true)

            val componentList = getCategoryLayoutResponse.components

            val data = CategoryL2TabData(
                title = categoryTitle,
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = categoryIdL2,
                componentList = componentList
            )

            val miniCartItemProduct = MiniCartItem.MiniCartItemProduct(
                productId = "6903919751",
                quantity = 5
            )

            val cartProductMap = MiniCartItemKey("6903919751") to miniCartItemProduct

            val miniCartData = MiniCartSimplifiedData(
                miniCartItems = mapOf(cartProductMap)
            )

            viewModel.onViewCreated(data)
            viewModel.onSuccessGetMiniCartData(miniCartData)

            val productItem = viewModel.visitableListLiveData.value.orEmpty()
                .filterIsInstance<ProductItemDataView>()[2]

            val product = productItem.productCardModel
            val shopId = productItem.shopId

            viewModel.onCartQuantityChanged(product, shopId, quantity, layoutType)
            advanceTimeBy(1000L)

            verifyUpdateCartUseCaseCalled()

            viewModel.updateToolbarNotification
                .verifyValueEquals(Unit)
        }
    }

    @Test
    fun `given quantity is zero when delete cart item should call delete cart use case and update toolbar notification`() {
        runTest {
            val quantity = 0
            val layoutType = "product_card_item"

            onGetProductList(thenReturn = getProductResponse)
            onGetProductAds(thenReturn = getProductAdsResponse)
            onDeleteCartItem_thenReturn(response = RemoveFromCartData())
            onGetIsUserLoggedIn_thenReturn(isLoggedIn = true)

            val componentList = getCategoryLayoutResponse.components

            val data = CategoryL2TabData(
                title = categoryTitle,
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = categoryIdL2,
                componentList = componentList
            )

            val miniCartItemProduct = MiniCartItem.MiniCartItemProduct(
                productId = "6903919751",
                quantity = 5
            )

            val cartProductMap = MiniCartItemKey("6903919751") to miniCartItemProduct

            val miniCartData = MiniCartSimplifiedData(
                miniCartItems = mapOf(cartProductMap)
            )

            viewModel.onViewCreated(data)
            viewModel.onSuccessGetMiniCartData(miniCartData)

            val productItem = viewModel.visitableListLiveData.value.orEmpty()
                .filterIsInstance<ProductItemDataView>()[2]

            val product = productItem.productCardModel
            val shopId = productItem.shopId

            viewModel.onCartQuantityChanged(product, shopId, quantity, layoutType)
            advanceTimeBy(1000L)

            verifyDeleteCartUseCaseCalled()

            viewModel.updateToolbarNotification
                .verifyValueEquals(Unit)
        }
    }

    private fun List<Visitable<*>>?.verifyProductOrderQuantity(index: Int, expectedProductQuantity: Int) {
        val visitableListLiveData = this.orEmpty()

        val actualProductList = visitableListLiveData.filterIsInstance<ProductItemDataView>()

        val expectedProductList = actualProductList.toMutableList()
        val productCardModel = expectedProductList[index].productCardModel.copy(orderQuantity = expectedProductQuantity)
        expectedProductList[index] = expectedProductList[index].copy(productCardModel = productCardModel)

        assertEquals(expectedProductList, actualProductList)
    }


    private fun List<Visitable<*>>?.verifyProductAdsOrderQuantity(index: Int, expectedProductQuantity: Int) {
        val visitableListLiveData = this.orEmpty()

        val actualProductAdsList = visitableListLiveData
            .filterIsInstance<TokoNowAdsCarouselUiModel>()
            .first()
            .items

        val expectedProductAdsList = actualProductAdsList.toMutableList()
        val adsProductCardModel = expectedProductAdsList[index].productCardModel.copy(orderQuantity = expectedProductQuantity)
        expectedProductAdsList[index] = expectedProductAdsList[index].copy(productCardModel = adsProductCardModel)

        assertEquals(expectedProductAdsList, actualProductAdsList)
    }
}
