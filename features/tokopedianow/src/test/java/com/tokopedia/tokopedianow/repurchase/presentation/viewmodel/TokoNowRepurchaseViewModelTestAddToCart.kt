package com.tokopedia.tokopedianow.repurchase.presentation.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAddToCartTracker
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper
import com.tokopedia.tokopedianow.repurchase.domain.model.TokoNowRepurchasePageResponse
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowRepurchaseViewModelTestAddToCart: TokoNowRepurchaseViewModelTestFixture() {

    companion object {
        private const val CHANGE_QUANTITY_DELAY = 500L
    }

    @Test
    fun `given mini cart item is null when onClickAddToCart should set miniCartAdd success`() {
        coroutineTestRule.runBlockingTest {
            val productId = "1"
            val quantity = 5
            val shopId = "5"
            val type = ""

            val productListResponse = TokoNowRepurchasePageResponse.GetRepurchaseProductListResponse(
                meta = TokoNowRepurchasePageResponse.GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(
                    RepurchaseProduct(
                        id = productId,
                        stock = 0,
                        shop = RepurchaseProduct.Shop(id = shopId)
                    )
                )
            )
            val addToCartResponse = AddToCartDataModel()

            onGetRepurchaseProductList_thenReturn(productListResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getLayoutData()
            viewModel.onCartQuantityChanged(productId, quantity, type, shopId)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyAddToCartUseCaseCalled()

            viewModel.addItemToCart
                .verifySuccessEquals(Success(AddToCartDataModel()))

            viewModel.updateToolbarNotification
                .verifyValueEquals(true)
        }
    }

    @Test
    fun `given mini cart item is null when addToCart error should set miniCartAdd fail`() {
        coroutineTestRule.runBlockingTest {
            val productId = "1"
            val quantity = 5
            val shopId = "5"
            val type = ""

            val addToCartError = NullPointerException()
            val productListResponse = TokoNowRepurchasePageResponse.GetRepurchaseProductListResponse(
                meta = TokoNowRepurchasePageResponse.GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(
                    RepurchaseProduct(
                        id = productId,
                        stock = 0,
                        shop = RepurchaseProduct.Shop(id = shopId)
                    )
                )
            )

            onGetRepurchaseProductList_thenReturn(productListResponse)
            onAddToCart_thenReturn(addToCartError)

            viewModel.getLayoutData()
            viewModel.onCartQuantityChanged(productId, quantity, type, shopId)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyAddToCartUseCaseCalled()

            viewModel.addItemToCart
                .verifyErrorEquals(Fail(addToCartError))
        }
    }

    @Test
    fun `given mini cart item is null when onClickAddToCart should hit add to cart tracker`() {
        coroutineTestRule.runBlockingTest {
            val productId1 = "1"
            val productId2 = "2"
            val quantity = 5
            val shopId = "5"
            val type = RepurchaseLayoutMapper.PRODUCT_REPURCHASE

            val productListResponse = TokoNowRepurchasePageResponse.GetRepurchaseProductListResponse(
                meta = TokoNowRepurchasePageResponse.GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(
                    RepurchaseProduct(
                        id = productId1,
                        stock = 0,
                        shop = RepurchaseProduct.Shop(id = shopId)
                    ), RepurchaseProduct(
                        id = productId2,
                        stock = 0,
                        shop = RepurchaseProduct.Shop(id = shopId)
                    )
                )
            )
            val addToCartResponse = AddToCartDataModel()

            onGetRepurchaseProductList_thenReturn(productListResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getLayoutData()
            viewModel.onCartQuantityChanged(productId2, quantity, type, shopId)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyAddToCartUseCaseCalled()

            viewModel.repurchaseAddToCartTracker
                .verifyValueEquals(
                    RepurchaseAddToCartTracker(
                        cartId = "",
                        quantity = quantity,
                        data = RepurchaseProductUiModel(
                            shopId = "5",
                            category = "",
                            categoryId = "",
                            parentId = "",
                            productCardModel = TokoNowProductCardViewUiModel(
                                productId = "2",
                                isSimilarProductShown = true,
                                isWishlistShown = true,
                                needToShowQuantityEditor = true
                            ),
                            position = 2
                        )
                    )
                )
        }
    }

    @Test
    fun `given mini cart item is null when onClickAddToCart should not hit add to cart tracker`() {
        coroutineTestRule.runBlockingTest {
            val productId1 = "1"
            val productId2 = "2"
            val productId3 = "3"
            val quantity = 5
            val shopId = "5"
            val type = RepurchaseLayoutMapper.PRODUCT_REPURCHASE

            val productListResponse = TokoNowRepurchasePageResponse.GetRepurchaseProductListResponse(
                meta = TokoNowRepurchasePageResponse.GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(
                    RepurchaseProduct(
                        id = productId1,
                        stock = 0,
                        shop = RepurchaseProduct.Shop(id = shopId)
                    ), RepurchaseProduct(
                        id = productId2,
                        stock = 0,
                        shop = RepurchaseProduct.Shop(id = shopId)
                    )
                )
            )
            val addToCartResponse = AddToCartDataModel()

            onGetRepurchaseProductList_thenReturn(productListResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getLayoutData()
            viewModel.onCartQuantityChanged(productId3, quantity, type, shopId)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyAddToCartUseCaseCalled()

            viewModel.repurchaseAddToCartTracker
                .verifyValueEquals(null)
        }
    }


    @Test
    fun `given quantity is 0 when onClickAddToCart should set miniCartRemove success`() {
        coroutineTestRule.runBlockingTest {
            val warehouseId = "1"
            val productId = "1"
            val quantity = 0
            val shopId = "5"
            val type = ""

            val productListResponse = TokoNowRepurchasePageResponse.GetRepurchaseProductListResponse(
                meta = TokoNowRepurchasePageResponse.GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(
                    RepurchaseProduct(
                        id = productId,
                        stock = 0,
                        shop = RepurchaseProduct.Shop(id = shopId)
                    )
                )
            )

            val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1))
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetUserLoggedIn_thenReturn(isLoggedIn = true)
            onGetRepurchaseProductList_thenReturn(productListResponse)
            onRemoveItemCart_thenReturn(RemoveFromCartData())
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.getLayoutData()
            viewModel.getMiniCart(listOf(shopId), warehouseId)
            viewModel.onCartQuantityChanged(productId, quantity, type, shopId)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetMiniCartUseCaseCalled()
            verifyDeleteCartUseCaseCalled()

            viewModel.removeCartItem
                .verifySuccessEquals(Success(Pair(productId, "")))

            viewModel.updateToolbarNotification
                .verifyValueEquals(true)
        }
    }

    @Test
    fun `given quantity is 0 when remove cart item error should set miniCartRemove fail`() {
        coroutineTestRule.runBlockingTest {
            val warehouseId = "1"
            val productId = "1"
            val quantity = 0
            val shopId = "5"
            val type = ""

            val removeItemCartError = NullPointerException()
            val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1))
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            val productListResponse = TokoNowRepurchasePageResponse.GetRepurchaseProductListResponse(
                meta = TokoNowRepurchasePageResponse.GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(
                    RepurchaseProduct(
                        id = productId,
                        stock = 0,
                        shop = RepurchaseProduct.Shop(id = shopId)
                    )
                )
            )

            onGetUserLoggedIn_thenReturn(isLoggedIn = true)
            onGetRepurchaseProductList_thenReturn(productListResponse)
            onRemoveItemCart_thenReturn(removeItemCartError)
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.getLayoutData()
            viewModel.getMiniCart(listOf(shopId), warehouseId)
            viewModel.onCartQuantityChanged(productId, quantity, type, shopId)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetMiniCartUseCaseCalled()
            verifyDeleteCartUseCaseCalled()

            viewModel.removeCartItem
                .verifyErrorEquals(Fail(removeItemCartError))
        }
    }

    @Test
    fun `given mini cart item is NOT null when onClickAddToCart should set miniCartUpdate success`() {
        coroutineTestRule.runBlockingTest {
            val warehouseId = "1"
            val productId = "1"
            val quantity = 5
            val shopId = "5"
            val type = ""

            val productListResponse = TokoNowRepurchasePageResponse.GetRepurchaseProductListResponse(
                meta = TokoNowRepurchasePageResponse.GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(
                    RepurchaseProduct(
                        id = productId,
                        stock = 0,
                        shop = RepurchaseProduct.Shop(id = shopId)
                    )
                )
            )

            val updateCartResponse = UpdateCartV2Data()
            val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1))
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetUserLoggedIn_thenReturn(isLoggedIn = true)
            onGetRepurchaseProductList_thenReturn(productListResponse)
            onUpdateItemCart_thenReturn(updateCartResponse)
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.getLayoutData()
            viewModel.getMiniCart(listOf(shopId), warehouseId)
            viewModel.onCartQuantityChanged(productId, quantity, type, shopId)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetMiniCartUseCaseCalled()
            verifyUpdateCartUseCaseCalled()

            viewModel.updateCartItem
                .verifySuccessEquals(Success(Triple(productId, updateCartResponse, quantity)))

            viewModel.updateToolbarNotification
                .verifyValueEquals(true)
        }
    }

    @Test
    fun `given mini cart item is NOT null when update cart item error should set miniCartUpdate fail`() {
        coroutineTestRule.runBlockingTest {
            val warehouseId = "1"
            val productId = "1"
            val quantity = 5
            val shopId = "5"
            val type = ""

            val updateCartError = NullPointerException()
            val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1))
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            val productListResponse = TokoNowRepurchasePageResponse.GetRepurchaseProductListResponse(
                meta = TokoNowRepurchasePageResponse.GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(
                    RepurchaseProduct(
                        id = productId,
                        stock = 0,
                        shop = RepurchaseProduct.Shop(id = shopId)
                    )
                )
            )

            onGetUserLoggedIn_thenReturn(isLoggedIn = true)
            onGetRepurchaseProductList_thenReturn(productListResponse)
            onUpdateItemCart_thenReturn(updateCartError)
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.getLayoutData()
            viewModel.getMiniCart(listOf(shopId), warehouseId)
            viewModel.onCartQuantityChanged(productId, quantity, type, shopId)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetMiniCartUseCaseCalled()
            verifyUpdateCartUseCaseCalled()

            viewModel.updateCartItem
                .verifyErrorEquals(Fail(updateCartError))
        }
    }

    @Test
    fun `given product not found in miniCartItems when onClickAddToCart should do nothing`() {
        coroutineTestRule.runBlockingTest {
            val warehouseId = "1"
            val productId = "1"
            val quantity = 0
            val shopId = "5"
            val type = ""

            val productListResponse = TokoNowRepurchasePageResponse.GetRepurchaseProductListResponse(
                meta = TokoNowRepurchasePageResponse.GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(
                    RepurchaseProduct(
                        id = productId,
                        stock = 0,
                        shop = RepurchaseProduct.Shop(id = shopId)
                    )
                )
            )

            val addToCartResponse = AddToCartDataModel()
            val miniCartItems = mapOf(MiniCartItemKey("5") to MiniCartItem.MiniCartItemProduct(productId = "5", quantity = 1))
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetUserLoggedIn_thenReturn(isLoggedIn = true)
            onGetRepurchaseProductList_thenReturn(productListResponse)
            onAddToCart_thenReturn(addToCartResponse)
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.getLayoutData()
            viewModel.getMiniCart(listOf(shopId), warehouseId)
            viewModel.onCartQuantityChanged(productId, quantity, type, shopId)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetMiniCartUseCaseCalled()

            viewModel.addItemToCart.verifyValueEquals(null)
        }
    }
}
