package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.data.createHomeProductCardUiModel
import com.tokopedia.tokopedianow.home.analytic.HomeAddToCartTracker
import com.tokopedia.tokopedianow.home.analytic.HomeRemoveFromCartTracker
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse
import com.tokopedia.tokopedianow.home.domain.model.Grid
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.Shop
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowHomeViewModelTestAddToCart : TokoNowHomeViewModelTestFixture() {

    companion object {
        private const val CHANGE_QUANTITY_DELAY = 700L
    }

    @Test
    fun `given mini cart item is null when onCartQuantityChanged should update product quantity`() {
        runTest {
            val channelId = "1001"
            val productId = "1"
            val quantity = 5
            val shopId = "5"
            val type = TokoNowLayoutType.REPURCHASE_PRODUCT

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "1001",
                    layout = "recent_purchase_tokonow",
                    header = Header(
                        name = "Kamu pernah beli",
                        serverTimeUnix = 0
                    )
                )
            )
            val repurchaseResponse = GetRepurchaseResponse.RepurchaseData(
                title = "Kamu pernah beli",
                products = listOf(
                    RepurchaseProduct(
                        id = productId,
                        stock = 5,
                        maxOrder = 10,
                        minOrder = 3
                    )
                )
            )
            val addToCartResponse = AddToCartDataModel()

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRepurchaseWidget_thenReturn(repurchaseResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onCartQuantityChanged(channelId, productId, quantity, shopId, 5, false, type)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val chooseAddressWidget = TokoNowChooseAddressWidgetUiModel(id = "0")
            val repurchaseUiModel = TokoNowRepurchaseUiModel(
                id = "1001",
                title = "Kamu pernah beli",
                productList = listOf(
                    createHomeProductCardUiModel(
                        channelId = channelId,
                        productId = productId,
                        quantity = 5,
                        stock = 5,
                        minOrder = 3,
                        maxOrder = 10,
                        position = 1,
                        originalPosition = 1,
                        headerName = "Kamu pernah beli"
                    )
                ),
                state = TokoNowLayoutState.SHOW
            )

            val homeLayoutItems = listOf(
                chooseAddressWidget,
                repurchaseUiModel
            )

            val expectedResult = Success(
                HomeLayoutListUiModel(
                    items = homeLayoutItems,
                    state = TokoNowLayoutState.UPDATE
                )
            )

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetRepurchaseWidgetUseCaseCalled()
            verifyAddToCartUseCaseCalled()

            viewModel.atcQuantity
                .verifySuccessEquals(expectedResult)

            viewModel.addItemToCart
                .verifySuccessEquals(Success(AddToCartDataModel()))

            viewModel.updateToolbarNotification
                .verifyValueEquals(true)
        }
    }

    @Test
    fun `given quantity is 0 when onCartQuantityChanged should update product quantity to 0`() {
        runTest {
            val warehouseId = "1"
            val channelId = "1001"
            val productId = "100"
            val quantity = 0
            val shopId = "5"
            val type = TokoNowLayoutType.REPURCHASE_PRODUCT

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "1001",
                    layout = "recent_purchase_tokonow",
                    header = Header(
                        name = "Kamu pernah beli",
                        serverTimeUnix = 0
                    )
                )
            )
            val repurchaseResponse = GetRepurchaseResponse.RepurchaseData(
                title = "Kamu pernah beli",
                products = listOf(
                    RepurchaseProduct(
                        id = productId,
                        stock = 5,
                        maxOrder = 4,
                        minOrder = 3
                    )
                )
            )
            val miniCartItems = mapOf(
                MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(
                    productId = productId,
                    quantity = 1
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)
            
            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRepurchaseWidget_thenReturn(repurchaseResponse)
            onGetMiniCart_thenReturn(miniCartResponse)
            onRemoveItemCart_thenReturn(RemoveFromCartData())
            onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.getMiniCart(listOf(shopId), warehouseId)
            viewModel.onCartQuantityChanged(channelId, productId, quantity, shopId, 1, false, type)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val repurchaseUiModel = TokoNowRepurchaseUiModel(
                id = "1001",
                title = "Kamu pernah beli",
                productList = listOf(
                    createHomeProductCardUiModel(
                        channelId = channelId,
                        productId = productId,
                        quantity = 0,
                        stock = 5,
                        minOrder = 3,
                        maxOrder = 4,
                        position = 1,
                        originalPosition = 1,
                        headerName = "Kamu pernah beli"
                    )
                ),
                state = TokoNowLayoutState.SHOW
            )

            val homeLayoutItems = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                repurchaseUiModel
            )

            val expectedResult = Success(
                HomeLayoutListUiModel(
                    items = homeLayoutItems,
                    state = TokoNowLayoutState.UPDATE
                )
            )

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetRepurchaseWidgetUseCaseCalled()
            verifyGetMiniCartUseCaseCalled()
            verifyDeleteCartUseCaseCalled()

            viewModel.atcQuantity
                .verifySuccessEquals(expectedResult)

            viewModel.removeCartItem
                .verifySuccessEquals(Success(Pair(productId, "")))

            viewModel.updateToolbarNotification
                .verifyValueEquals(true)
        }
    }

    @Test
    fun `given mini cart item is NOT null when onCartQuantityChanged should update product quantity`() {
        runTest {
            val warehouseId = "1"
            val channelId = "1001"
            val productId = "100"
            val shopId = "5"
            val type = TokoNowLayoutType.REPURCHASE_PRODUCT

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "1001",
                    layout = "recent_purchase_tokonow",
                    header = Header(
                        name = "Kamu pernah beli",
                        serverTimeUnix = 0
                    )
                )
            )
            val repurchaseResponse = GetRepurchaseResponse.RepurchaseData(
                title = "Kamu pernah beli",
                products = listOf(RepurchaseProduct(id = productId, maxOrder = 5, minOrder = 3))
            )
            val miniCartItems = mapOf(
                MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(
                    productId = productId,
                    quantity = 1
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRepurchaseWidget_thenReturn(repurchaseResponse)
            onGetMiniCart_thenReturn(miniCartResponse)
            onUpdateItemCart_thenReturn(UpdateCartV2Data())
            onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onScroll(2, LocalCacheModel(), listOf(), true)
            viewModel.getMiniCart(listOf(shopId), warehouseId)
            viewModel.onCartQuantityChanged(channelId, productId, 4, shopId, 1, false, type)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val expected = Success(Triple(productId, UpdateCartV2Data(), 4))

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetRepurchaseWidgetUseCaseCalled()
            verifyGetMiniCartUseCaseCalled()
            verifyUpdateCartUseCaseCalled()

            viewModel.updateCartItem
                .verifySuccessEquals(expected)

            viewModel.updateToolbarNotification
                .verifyValueEquals(true)
        }
    }

    @Test
    fun `when add product recom to cart should track add product recom to cart`() {
        runTest {
            val channelId = "1001"
            val productId = "2"
            val quantity = 5
            val shopId = "5"
            val cartId = "1999"
            val type = TokoNowLayoutType.PRODUCT_RECOM

            val homeRecomResponse = HomeLayoutResponse(
                id = "1001",
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                ),
                grids = listOf(Grid(id = "1"), Grid(id = "2"))
            )
            val homeLayoutResponse = listOf(homeRecomResponse)

            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = cartId))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onCartQuantityChanged(channelId, productId, quantity, shopId, 1, false, type)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val productList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    productCardModel = ProductCardCompactUiModel(
                        productId = "1",
                        price = "0",
                        orderQuantity = 0,
                        usePreDraw = true,
                        needToShowQuantityEditor = true
                    ),
                    shopType = "pm"
                ),
                ProductCardCompactCarouselItemUiModel(
                    productCardModel = ProductCardCompactUiModel(
                        productId = "2",
                        price = "0",
                        orderQuantity = quantity,
                        usePreDraw = true,
                        needToShowQuantityEditor = true
                    ),
                    shopType = "pm"
                )
            )
            val realTimeRecom =
                HomeRealTimeRecomUiModel(channelId = channelId, headerName = "Lagi Diskon")

            val homeRecomUiModel = HomeProductRecomUiModel(
                id = "1001",
                title = "Lagi Diskon",
                productList = productList,
                realTimeRecom = realTimeRecom,
                seeMoreModel = ProductCardCompactCarouselSeeMoreUiModel(
                    headerName = "Lagi Diskon"
                ),
                headerModel = TokoNowDynamicHeaderUiModel(
                    title = "Lagi Diskon"
                )
            )

            val expectedResult = HomeAddToCartTracker(
                position = 1,
                quantity = quantity,
                cartId = cartId,
                data = homeRecomUiModel
            )

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyAddToCartUseCaseCalled()

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expectedResult)
        }
    }

    @Test
    fun `when update product recom cart item should track update product recom`() {
        runTest {
            val warehouseId = "1"
            val channelId = "1001"
            val productId = "1"
            val shopId = "5"
            val cartId = "1999"
            val type = TokoNowLayoutType.PRODUCT_RECOM

            val homeRecomResponse = HomeLayoutResponse(
                id = "1001",
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                ),
                grids = listOf(Grid(id = "1"), Grid(id = "2"))
            )

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "1001",
                    layout = "top_carousel_tokonow",
                    header = Header(
                        name = "Lagi Diskon",
                        serverTimeUnix = 0
                    )
                ),
                homeRecomResponse
            )

            val updateCartResponse = UpdateCartV2Data()
            val miniCartItems = mapOf(
                MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(
                    productId = productId,
                    quantity = 1,
                    cartId = cartId
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetMiniCart_thenReturn(miniCartResponse)
            onUpdateItemCart_thenReturn(updateCartResponse)
            onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onScroll(2, LocalCacheModel(), listOf(), true)
            viewModel.getMiniCart(listOf(shopId), warehouseId)
            viewModel.onCartQuantityChanged(channelId, productId, 4, shopId, 1, false, type)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val productList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    productCardModel = ProductCardCompactUiModel(
                        productId = "1",
                        orderQuantity = 4,
                        usePreDraw = true,
                        price = "0",
                        needToShowQuantityEditor = true
                    ),
                    shopType = "pm"
                ),
                ProductCardCompactCarouselItemUiModel(
                    productCardModel = ProductCardCompactUiModel(
                        productId = "2",
                        usePreDraw = true,
                        price = "0",
                        needToShowQuantityEditor = true
                    ),
                    shopType = "pm"
                )
            )

            val realTimeRecom = HomeRealTimeRecomUiModel(
                channelId = channelId,
                headerName = "Lagi Diskon"
            )

            val homeRecomUiModel = HomeProductRecomUiModel(
                id = "1001",
                title = "Lagi Diskon",
                productList = productList,
                realTimeRecom = realTimeRecom,
                seeMoreModel = ProductCardCompactCarouselSeeMoreUiModel(
                    headerName = "Lagi Diskon"
                ),
                headerModel = TokoNowDynamicHeaderUiModel(
                    title = "Lagi Diskon"
                )
            )

            val expected = HomeAddToCartTracker(
                position = 0,
                quantity = 4,
                cartId = cartId,
                data = homeRecomUiModel
            )

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetMiniCartUseCaseCalled()
            verifyUpdateCartUseCaseCalled()

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expected)
        }
    }

    @Test
    fun `given update cart error when update product cart item should set miniCartRemove value fail`() {
        runTest {
            val error = NullPointerException()
            val warehouseId = "1"
            val channelId = "1001"
            val productId = "1"
            val shopId = "5"
            val cartId = "1999"
            val type = TokoNowLayoutType.PRODUCT_RECOM

            val homeRecomResponse = HomeLayoutResponse(
                id = "1001",
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                ),
                grids = listOf(Grid(id = "1"), Grid(id = "2"))
            )

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "1001",
                    layout = "top_carousel_tokonow",
                    header = Header(
                        name = "Lagi Diskon",
                        serverTimeUnix = 0
                    )
                ),
                homeRecomResponse
            )

            val miniCartItems = mapOf(
                MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(
                    productId = productId,
                    quantity = 1,
                    cartId = cartId
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetMiniCart_thenReturn(miniCartResponse)
            onUpdateItemCart_thenReturn(error)
            onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onScroll(2, LocalCacheModel(), listOf(), true)
            viewModel.getMiniCart(listOf(shopId), warehouseId)
            viewModel.onCartQuantityChanged(channelId, productId, 4, shopId, 1, false, type)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetMiniCartUseCaseCalled()
            verifyUpdateCartUseCaseCalled()

            viewModel.updateCartItem
                .verifyErrorEquals(Fail(error))
        }
    }

    @Test
    fun `when remove product recom from cart should track remove product recom`() {
        runTest {
            val warehouseId = "1"
            val channelId = "1001"
            val productId = "1"
            val shopId = "5"
            val cartId = "1999"
            val type = TokoNowLayoutType.PRODUCT_RECOM

            val homeRecomResponse = HomeLayoutResponse(
                id = "1001",
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                ),
                grids = listOf(Grid(id = "1"), Grid(id = "2"))
            )

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "1001",
                    layout = "top_carousel_tokonow",
                    header = Header(
                        name = "Lagi Diskon",
                        serverTimeUnix = 0
                    )
                ),
                homeRecomResponse
            )

            val miniCartItems = mapOf(
                MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(
                    productId = productId,
                    quantity = 1,
                    cartId = cartId
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetMiniCart_thenReturn(miniCartResponse)
            onRemoveItemCart_thenReturn(RemoveFromCartData())
            onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onScroll(2, LocalCacheModel(), listOf(), true)
            viewModel.getMiniCart(listOf(shopId), warehouseId)
            viewModel.onCartQuantityChanged(channelId, productId, 0, shopId, 1, false, type)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val productList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    productCardModel = ProductCardCompactUiModel(
                        productId = "1",
                        price = "0",
                        orderQuantity = 0,
                        usePreDraw = true,
                        needToShowQuantityEditor = true
                    ),
                    shopType = "pm",

                    ),
                ProductCardCompactCarouselItemUiModel(
                    productCardModel = ProductCardCompactUiModel(
                        productId = "2",
                        price = "0",
                        orderQuantity = 0,
                        usePreDraw = true,
                        needToShowQuantityEditor = true
                    ),
                    shopType = "pm"
                )
            )
            val realTimeRecom =
                HomeRealTimeRecomUiModel(channelId = channelId, headerName = "Lagi Diskon")

            val homeRecomUiModel = HomeProductRecomUiModel(
                id = "1001",
                title = "Lagi Diskon",
                productList = productList,
                realTimeRecom = realTimeRecom,
                seeMoreModel = ProductCardCompactCarouselSeeMoreUiModel(
                    headerName = "Lagi Diskon"
                ),
                headerModel = TokoNowDynamicHeaderUiModel(
                    title = "Lagi Diskon"
                )
            )

            val expected = HomeRemoveFromCartTracker(
                position = 0,
                quantity = 0,
                cartId = cartId,
                data = homeRecomUiModel
            )

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetMiniCartUseCaseCalled()
            verifyDeleteCartUseCaseCalled()

            viewModel.homeRemoveFromCartTracker
                .verifyValueEquals(expected)
        }
    }

    @Test
    fun `homeLayoutItemList does NOT contain product recom when remove from cart should NOT track the product`() {
        runTest {
            val warehouseId = "1"
            val channelId = "1001"
            val productId = "1"
            val shopId = "5"
            val cartId = "1999"
            val type = TokoNowLayoutType.PRODUCT_RECOM

            val miniCartItems = mapOf(
                MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(
                    productId = productId,
                    quantity = 1,
                    cartId = cartId
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetMiniCart_thenReturn(miniCartResponse)
            onRemoveItemCart_thenReturn(RemoveFromCartData())
            onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

            viewModel.getMiniCart(listOf(shopId), warehouseId)
            viewModel.onCartQuantityChanged(channelId, productId, 0, shopId, 1, false, type)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.homeRemoveFromCartTracker
                .verifyValueEquals(null)
        }
    }

    @Test
    fun `given delete cart error when remove product from cart should set miniCartRemove fail`() {
        runTest {
            val error = NullPointerException()
            val warehouseId = "1"
            val channelId = "1001"
            val productId = "1"
            val shopId = "5"
            val cartId = "1999"
            val type = TokoNowLayoutType.PRODUCT_RECOM

            val homeRecomResponse = HomeLayoutResponse(
                id = "1001",
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                ),
                grids = listOf(Grid(id = "1"), Grid(id = "2"))
            )

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "1001",
                    layout = "top_carousel_tokonow",
                    header = Header(
                        name = "Lagi Diskon",
                        serverTimeUnix = 0
                    )
                ),
                homeRecomResponse
            )

            val miniCartItems = mapOf(
                MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(
                    productId = productId,
                    quantity = 1,
                    cartId = cartId
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetMiniCart_thenReturn(miniCartResponse)
            onRemoveItemCart_thenReturn(error)
            onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onScroll(2, LocalCacheModel(), listOf(), true)
            viewModel.getMiniCart(listOf(shopId), warehouseId)
            viewModel.onCartQuantityChanged(channelId, productId, 0, shopId, 1, false, type)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetMiniCartUseCaseCalled()
            verifyDeleteCartUseCaseCalled()

            viewModel.removeCartItem
                .verifyErrorEquals(Fail(error))
        }
    }

    @Test
    fun `given homeLayoutResponse does NOT contain product recom when add to cart should NOT track add product`() {
        runTest {
            val channelId = "1001"
            val productId = "2"
            val quantity = 5
            val shopId = "5"
            val cartId = "1999"
            val type = TokoNowLayoutType.PRODUCT_RECOM

            val homeLayoutResponse = emptyList<HomeLayoutResponse>()
            val homeRecomResponse = HomeLayoutResponse(
                id = "1001",
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                ),
                grids = listOf(Grid(id = "1"), Grid(id = "2"))
            )

            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = cartId))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

            onGetHomeLayoutData_thenReturn(listOf(homeRecomResponse))

            viewModel.onScroll(2, LocalCacheModel(), listOf(), true)
            viewModel.onCartQuantityChanged(channelId, productId, quantity, shopId, 1, false, type)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyAddToCartUseCaseCalled()

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expected = null)
        }
    }

    @Test
    fun `given product NOT found when add product recom to cart should NOT track add product`() {
        runTest {
            val channelId = "1001"
            val quantity = 5
            val shopId = "5"
            val cartId = "1999"
            val type = TokoNowLayoutType.PRODUCT_RECOM

            val homeRecomResponse = HomeLayoutResponse(
                id = "1001",
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                ),
                grids = listOf(Grid(id = "1"), Grid(id = "2"))
            )

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "1001",
                    layout = "top_carousel_tokonow",
                    header = Header(
                        name = "Lagi Diskon",
                        serverTimeUnix = 0
                    )
                ),
                homeRecomResponse
            )

            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = cartId))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onScroll(2, LocalCacheModel(), listOf(), true)
            viewModel.onCartQuantityChanged(channelId, "3", quantity, shopId, 1, false, type)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyAddToCartUseCaseCalled()

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expected = null)
        }
    }

    @Test
    fun `given recommendation list is empty when add product recom to cart should NOT track add product`() {
        runTest {
            val channelId = "1001"
            val quantity = 5
            val shopId = "5"
            val cartId = "1999"
            val type = TokoNowLayoutType.PRODUCT_RECOM

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "1001",
                    layout = "top_carousel_tokonow",
                    header = Header(
                        name = "Lagi Diskon",
                        serverTimeUnix = 0
                    )
                )
            )

            val homeRecomResponse = HomeLayoutResponse(
                id = "1001",
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                ),
                grids = emptyList()
            )

            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = cartId))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

            onGetHomeLayoutData_thenReturn(listOf(homeRecomResponse))

            viewModel.onScroll(2, LocalCacheModel(), listOf(), true)
            viewModel.onCartQuantityChanged(channelId, "3", quantity, shopId, 1, false, type)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyAddToCartUseCaseCalled()

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expected = null)
        }
    }

    @Test
    fun `when add repurchase product to cart should track add repurchase product`() {
        runTest {
            val channelId = "1001"

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "1001",
                    layout = "recent_purchase_tokonow",
                    header = Header(
                        name = "Kamu pernah beli",
                        serverTimeUnix = 0
                    )
                )
            )

            val repurchaseResponse = GetRepurchaseResponse.RepurchaseData(
                title = "Kamu pernah beli",
                products = listOf(
                    RepurchaseProduct(
                        id = "1",
                        stock = 5,
                        maxOrder = 4,
                        minOrder = 3
                    ),
                    RepurchaseProduct(
                        id = "2",
                        stock = 3,
                        maxOrder = 4,
                        minOrder = 1
                    )
                )
            )

            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRepurchaseWidget_thenReturn(repurchaseResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onScroll(2, LocalCacheModel(), listOf(), true)
            viewModel.onCartQuantityChanged(
                channelId,
                "2",
                2,
                "100",
                1,
                false,
                TokoNowLayoutType.REPURCHASE_PRODUCT
            )
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val productCardUiModel = createHomeProductCardUiModel(
                channelId = channelId,
                productId = "2",
                quantity = 2,
                stock = 3,
                minOrder = 1,
                maxOrder = 4,
                position = 2,
                originalPosition = 2,
                headerName = "Kamu pernah beli"
            )

            val expected = HomeAddToCartTracker(
                position = 2,
                quantity = 2,
                cartId = "1999",
                productCardUiModel
            )

            verifyGetRepurchaseWidgetUseCaseCalled()
            verifyAddToCartUseCaseCalled()

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expected)
        }
    }

    @Test
    fun `when add repurchase product to cart should track add old repurchase product`() {
        runTest {
            val channelId = "1001"

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "1001",
                    layout = "recent_purchase_tokonow",
                    header = Header(
                        name = "Kamu pernah beli",
                        serverTimeUnix = 0
                    )
                )
            )

            val repurchaseResponse = GetRepurchaseResponse.RepurchaseData(
                title = "Kamu pernah beli",
                products = listOf(
                    RepurchaseProduct(
                        id = "1",
                        stock = 5,
                        maxOrder = 4,
                        minOrder = 3
                    ),
                    RepurchaseProduct(
                        id = "2",
                        stock = 3,
                        maxOrder = 4,
                        minOrder = 1
                    )
                )
            )

            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRepurchaseWidget_thenReturn(repurchaseResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf(), false)
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onScroll(2, LocalCacheModel(), listOf(), false)
            viewModel.onCartQuantityChanged(
                channelId,
                "2",
                2,
                "100",
                1,
                false,
                TokoNowLayoutType.REPURCHASE_PRODUCT
            )
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val productCardUiModel = createHomeProductCardUiModel(
                channelId = channelId,
                productId = "2",
                stock = 3,
                quantity = 2,
                product = ProductCardModel(
                    hasAddToCartButton = false,
                    nonVariant = ProductCardModel.NonVariant(2, 1, 4)
                ),
                position = 2,
                headerName = "Kamu pernah beli"
            )

            val expected = HomeAddToCartTracker(
                position = 2,
                quantity = 2,
                cartId = "1999",
                productCardUiModel
            )

            verifyGetRepurchaseWidgetUseCaseCalled()
            verifyAddToCartUseCaseCalled()

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expected)
        }
    }

    @Test
    fun `given product not found when add repurchase product to cart should NOT track add to cart`() {
        runTest {
            val channelId = "1001"

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "1001",
                    layout = "recent_purchase_tokonow",
                    header = Header(
                        name = "Kamu pernah beli",
                        serverTimeUnix = 0
                    )
                )
            )

            val repurchaseResponse = GetRepurchaseResponse.RepurchaseData(
                title = "Kamu pernah beli",
                products = listOf(
                    RepurchaseProduct(
                        id = "1",
                        maxOrder = 5,
                        minOrder = 3
                    ),
                    RepurchaseProduct(
                        id = "2",
                        maxOrder = 3,
                        minOrder = 1
                    )
                )
            )

            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRepurchaseWidget_thenReturn(repurchaseResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onScroll(2, LocalCacheModel(), listOf(), true)
            viewModel.onCartQuantityChanged(
                channelId,
                "4",
                2,
                "100",
                1,
                false,
                TokoNowLayoutType.REPURCHASE_PRODUCT
            )
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetRepurchaseWidgetUseCaseCalled()
            verifyAddToCartUseCaseCalled()

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expected = null)
        }
    }

    @Test
    fun `given layout list does NOT contain repurchase when add product to cart should NOT track add to cart`() {
        runTest {
            val channelId = "1001"
            val homeLayoutResponse = emptyList<HomeLayoutResponse>()
            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onScroll(2, LocalCacheModel(), listOf(), true)
            viewModel.onCartQuantityChanged(
                channelId,
                "4",
                2,
                "100",
                1,
                false,
                TokoNowLayoutType.REPURCHASE_PRODUCT
            )
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyAddToCartUseCaseCalled()

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expected = null)
        }
    }

    @Test
    fun `when add mix left atc product to cart should track add mix left atc product`() {
        runTest {
            val channelId = "1001"

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "2122",
                    layout = "left_carousel_atc",
                    header = Header(
                        applink = "tokopedia://now",
                        name = "Mix Left Carousel",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = "2",
                            shop = Shop(shopId = "100")
                        )
                    )
                )
            )

            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onCartQuantityChanged(
                channelId,
                "2",
                2,
                "100",
                1,
                false,
                TokoNowLayoutType.MIX_LEFT_CAROUSEL_ATC
            )
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val productCardUiModel = HomeLeftCarouselAtcProductCardUiModel(
                id = "2",
                channelHeaderName = "Mix Left Carousel",
                shopId = "100",
                channelId = "2122",
                productCardModel = ProductCardCompactUiModel(
                    productId = "2",
                    price = "0",
                    usePreDraw = true,
                    needToShowQuantityEditor = true,
                    orderQuantity = 2
                ),
                position = 0
            )

            val expected = HomeAddToCartTracker(
                position = 0,
                quantity = 2,
                cartId = "1999",
                productCardUiModel
            )

            verifyAddToCartUseCaseCalled()

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expected)
        }
    }

    @Test
    fun `when add mix left atc product to cart should not track add mix left atc product`() {
        runTest {
            val channelId = "1001"

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "2122",
                    layout = "left_carousel_atc",
                    header = Header(
                        applink = "tokopedia://now",
                        name = "Mix Left Carousel",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = "2",
                            shop = Shop(shopId = "100")
                        )
                    )
                )
            )

            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onCartQuantityChanged(
                channelId,
                "4",
                2,
                "100",
                1,
                false,
                TokoNowLayoutType.MIX_LEFT_CAROUSEL_ATC
            )
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.homeAddToCartTracker
                .verifyValueEquals(null)
        }
    }

    @Test
    fun `given product not found when add mix left product to cart should NOT track add to cart`() {
        runTest {
            val channelId = "1001"

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "2122",
                    layout = "left_carousel",
                    header = Header(
                        name = "Mix Left Carousel",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = "2",
                            shop = Shop(shopId = "100")
                        )
                    )
                )
            )

            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onScroll(2, LocalCacheModel(), listOf(), true)
            viewModel.onCartQuantityChanged(
                channelId,
                "4",
                2,
                "100",
                1,
                false,
                TokoNowLayoutType.MIX_LEFT_CAROUSEL_ATC
            )
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyAddToCartUseCaseCalled()

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expected = null)
        }
    }

    @Test
    fun `given no product in response grid when add mix left product to cart should NOT track add to cart`() {
        runTest {
            val channelId = "1001"

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "2122",
                    layout = "left_carousel_atc",
                    header = Header(
                        name = "Mix Left Carousel",
                        serverTimeUnix = 0
                    ),
                    grids = listOf()
                )
            )

            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onScroll(2, LocalCacheModel(), listOf(), true)
            viewModel.onCartQuantityChanged(
                channelId,
                "4",
                2,
                "100",
                1,
                false,
                TokoNowLayoutType.MIX_LEFT_CAROUSEL_ATC
            )
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyAddToCartUseCaseCalled()

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expected = null)
        }
    }

    @Test
    fun `given layout list does NOT contain mix left when add product to cart should NOT track add to cart`() {
        runTest {
            val channelId = "1001"
            val homeLayoutResponse = emptyList<HomeLayoutResponse>()
            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onScroll(2, LocalCacheModel(), listOf(), true)
            viewModel.onCartQuantityChanged(
                channelId,
                "4",
                2,
                "100",
                1,
                false,
                TokoNowLayoutType.MIX_LEFT_CAROUSEL_ATC
            )
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyAddToCartUseCaseCalled()

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expected = null)
        }
    }

    @Test
    fun `given add to cart error when onCartQuantityChanged should set miniCartAdd value fail`() {
        runTest {
            val error = NullPointerException()
            val invalidLayoutType = "random layout type"
            val channelId = "1001"
            val productId = "4"
            val quantity = 2
            val shopId = "100"

            onAddToCart_thenReturn(error)

            viewModel.onCartQuantityChanged(channelId, productId, quantity, shopId, 1, false, invalidLayoutType)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.addItemToCart
                .verifyErrorEquals(Fail(error))
        }
    }

    @Test
    fun `when layout type is NOT valid should NOT track add to cart`() {
        runTest {
            val invalidLayoutType = "random layout type"
            val channelId = "1001"
            val productId = "4"
            val quantity = 2
            val shopId = "100"

            onAddToCart_thenReturn(AddToCartDataModel())

            viewModel.onCartQuantityChanged(channelId, productId, quantity, shopId, 1, false, invalidLayoutType)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expected = null)
        }
    }

    @Test
    fun `when quatity is zero and minicart is null should do nothing`() {
        runTest {
            val invalidLayoutType = "random layout type"
            val channelId = "1001"
            val productId = "4"
            val quantity = 0
            val shopId = "100"

            viewModel.onCartQuantityChanged(channelId, productId, quantity, shopId, 1, false, invalidLayoutType)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.addItemToCart
                .verifyValueEquals(expected = null)
        }
    }

    @Test
    fun `when add repurchase product to cart should move product position to last`() {
        runTest {
            val channelId = "1001"
            val productId = "1"
            val quantity = 5
            val shopId = "5"
            val type = TokoNowLayoutType.REPURCHASE_PRODUCT

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = "1001",
                    layout = "recent_purchase_tokonow",
                    header = Header(
                        name = "Kamu pernah beli",
                        serverTimeUnix = 0
                    )
                )
            )
            val repurchaseResponse = GetRepurchaseResponse.RepurchaseData(
                title = "Kamu pernah beli",
                products = listOf(
                    RepurchaseProduct(
                        id = productId,
                        stock = 5,
                        maxOrder = 10,
                        minOrder = 3
                    ),
                    RepurchaseProduct(
                        id = "3",
                        stock = 2,
                        maxOrder = 2,
                        minOrder = 1
                    )
                )
            )
            val addToCartResponse = AddToCartDataModel()

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRepurchaseWidget_thenReturn(repurchaseResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onCartQuantityChanged(channelId, productId, quantity, shopId, 5, false, type)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val chooseAddressWidget = TokoNowChooseAddressWidgetUiModel(id = "0")
            val repurchaseUiModel = TokoNowRepurchaseUiModel(
                id = "1001",
                title = "Kamu pernah beli",
                productList = listOf(
                    createHomeProductCardUiModel(
                        channelId = channelId,
                        productId = "3",
                        quantity = 0,
                        stock = 2,
                        minOrder = 1,
                        maxOrder = 2,
                        position = 1,
                        originalPosition = 2,
                        headerName = "Kamu pernah beli"
                    ),
                    createHomeProductCardUiModel(
                        channelId = channelId,
                        productId = productId,
                        quantity = 5,
                        stock = 5,
                        minOrder = 3,
                        maxOrder = 10,
                        position = 2,
                        originalPosition = 1,
                        headerName = "Kamu pernah beli"
                    )
                ),
                state = TokoNowLayoutState.SHOW
            )

            val homeLayoutItems = listOf(
                chooseAddressWidget,
                repurchaseUiModel
            )

            val expectedResult = Success(
                HomeLayoutListUiModel(
                    items = homeLayoutItems,
                    state = TokoNowLayoutState.UPDATE
                )
            )

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetRepurchaseWidgetUseCaseCalled()
            verifyAddToCartUseCaseCalled()

            viewModel.atcQuantity
                .verifySuccessEquals(expectedResult)
        }
    }
}
