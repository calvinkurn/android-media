package com.tokopedia.tokopedianow.common.base.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class BaseTokoNowViewModelTest : BaseTokoNowViewModelTestFixture() {

    companion object {
        private const val CHANGE_QUANTITY_DELAY = 700L
    }

    @Test
    fun `given mini cart item is null when onQuantityChanged should set miniCartAdd success`() {
        runTest {
            val productId = "1"
            val quantity = 5
            val shopId = "5"
            val stock = 0
            val addToCartResponse = AddToCartDataModel()

            onAddToCart_thenReturn(addToCartResponse)

            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, false)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyAddToCartUseCaseCalled()

            viewModel.addItemToCart
                .verifySuccessEquals(Success(AddToCartDataModel()))
        }
    }

    @Test
    fun `given mini cart item is null when addToCart error should set miniCartAdd fail`() {
        runTest {
            val productId = "1"
            val quantity = 5
            val shopId = "5"
            val stock = 0
            val addToCartError = NullPointerException()

            onAddToCart_thenReturn(addToCartError)

            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, false)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyAddToCartUseCaseCalled()

            viewModel.addItemToCart
                .verifyErrorEquals(Fail(addToCartError))
        }
    }

    @Test
    fun `given quantity is 0 when onQuantityChanged should set miniCartRemove success`() {
        runTest {
            val productId = "1"
            val quantity = 0
            val shopId = "5"
            val stock = 0
            val warehouseId = 5L

            val miniCartItems = mapOf(
                MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(
                    productId = productId,
                    quantity = 1
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetShopId_thenReturn(shopId.toLong())
            onGetWarehouseId_thenReturn(warehouseId)
            onGetUserLoggedIn_thenReturn(isLoggedIn = true)

            onRemoveItemCart_thenReturn(RemoveFromCartData())
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.getMiniCart()
            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, false)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetMiniCartUseCaseCalled()
            verifyDeleteCartUseCaseCalled()

            viewModel.removeCartItem
                .verifySuccessEquals(Success(Pair(productId, "")))
        }
    }

    @Test
    fun `given quantity is 0 when remove cart item error should set miniCartRemove fail`() {
        runTest {
            val productId = "1"
            val quantity = 0
            val shopId = "5"
            val stock = 0
            val warehouseId = 7L

            val removeItemCartError = NullPointerException()
            val miniCartItems = mapOf(
                MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(
                    productId = productId,
                    quantity = 1
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetShopId_thenReturn(shopId.toLong())
            onGetWarehouseId_thenReturn(warehouseId)
            onGetUserLoggedIn_thenReturn(isLoggedIn = true)

            onRemoveItemCart_thenReturn(removeItemCartError)
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.getMiniCart()
            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, false)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetMiniCartUseCaseCalled()
            verifyDeleteCartUseCaseCalled()

            viewModel.removeCartItem
                .verifyErrorEquals(Fail(removeItemCartError))
        }
    }

    @Test
    fun `given mini cart item is NOT null when onQuantityChanged should set miniCartUpdate success`() {
        runTest {
            val productId = "1"
            val quantity = 5
            val shopId = "5"
            val stock = 0
            val warehouseId = 1L

            val updateCartResponse = UpdateCartV2Data()
            val miniCartItems = mapOf(
                MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(
                    productId = productId,
                    quantity = 1
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetShopId_thenReturn(shopId.toLong())
            onGetWarehouseId_thenReturn(warehouseId)
            onGetUserLoggedIn_thenReturn(isLoggedIn = true)

            onUpdateItemCart_thenReturn(updateCartResponse)
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.getMiniCart()
            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, false)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetMiniCartUseCaseCalled()
            verifyUpdateCartUseCaseCalled()

            viewModel.updateCartItem
                .verifySuccessEquals(Success(Triple(productId, updateCartResponse, quantity)))
        }
    }

    @Test
    fun `given mini cart item is NOT null when update cart item error should set miniCartUpdate fail`() {
        runTest {
            val productId = "1"
            val quantity = 5
            val shopId = "5"
            val stock = 0
            val warehouseId = 5L

            val updateCartError = NullPointerException()
            val miniCartItems = mapOf(
                MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(
                    productId = productId,
                    quantity = 1
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetShopId_thenReturn(shopId.toLong())
            onGetWarehouseId_thenReturn(warehouseId)
            onGetUserLoggedIn_thenReturn(isLoggedIn = true)

            onUpdateItemCart_thenReturn(updateCartError)
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.getMiniCart()
            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, false)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetMiniCartUseCaseCalled()
            verifyUpdateCartUseCaseCalled()

            viewModel.updateCartItem
                .verifyErrorEquals(Fail(updateCartError))
        }
    }

    @Test
    fun `given product not found in miniCartItems when onQuantityChanged should add product to cart`() {
        runTest {
            val productId = "1"
            val quantity = 2
            val shopId = "5"
            val stock = 0
            val warehouseId = 9L

            val addToCartResponse = AddToCartDataModel()
            val miniCartItems = mapOf(
                MiniCartItemKey("5") to MiniCartItem.MiniCartItemProduct(
                    productId = "5",
                    quantity = 1
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetShopId_thenReturn(shopId.toLong())
            onGetWarehouseId_thenReturn(warehouseId)
            onGetUserLoggedIn_thenReturn(isLoggedIn = true)

            onAddToCart_thenReturn(addToCartResponse)
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.getMiniCart()
            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, false)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetMiniCartUseCaseCalled()
            verifyAddToCartUseCaseCalled()

            viewModel.addItemToCart
                .verifySuccessEquals(Success(addToCartResponse))
        }
    }

    @Test
    fun `given onQuantityChanged called twice when add to cart should call use case once`() {
        runTest {
            val productId = "1"
            val quantity = 1
            val shopId = "5"
            val stock = 0
            val warehouseId = 3L

            val addToCartResponse = AddToCartDataModel()
            val miniCartItems = mapOf(
                MiniCartItemKey("5") to MiniCartItem.MiniCartItemProduct(
                    productId = "5",
                    quantity = 1
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetShopId_thenReturn(shopId.toLong())
            onGetWarehouseId_thenReturn(warehouseId)
            onGetUserLoggedIn_thenReturn(isLoggedIn = true)

            onAddToCart_thenReturn(addToCartResponse)
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.getMiniCart()
            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, false)
            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, false)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetMiniCartUseCaseCalled()
            verifyAddToCartUseCaseCalled(times = 1)

            viewModel.addItemToCart
                .verifySuccessEquals(Success(addToCartResponse))
        }
    }

    @Test
    fun `given new quantity same as mini cart quantity when onQuantityChanged should do nothing`() {
        coroutineTestRule.runTest {
            val productId = "5"
            val newQuantity = 1
            val miniCartQuantity = 1
            val shopId = "5"
            val stock = 0
            val warehouseId = 3L

            val miniCartItems = mapOf(
                MiniCartItemKey("5") to MiniCartItem.MiniCartItemProduct(
                    productId = "5",
                    quantity = miniCartQuantity
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetShopId_thenReturn(shopId.toLong())
            onGetWarehouseId_thenReturn(warehouseId)
            onGetUserLoggedIn_thenReturn(isLoggedIn = true)
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.getMiniCart()
            viewModel.onCartQuantityChanged(productId, shopId, newQuantity, stock, false)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyAddToCartUseCaseNotCalled()
            verifyUpdateCartUseCaseNotCalled()
            verifyDeleteCartUseCaseNotCalled()

            viewModel.addItemToCart
                .verifyValueEquals(null)

            viewModel.updateCartItem
                .verifyValueEquals(null)

            viewModel.removeCartItem
                .verifyValueEquals(null)
        }
    }

    @Test
    fun `given new quantity same as current quantity when onCartQuantityChanged should do nothing`() {
        runTest {
            val productId = "5"
            val newQuantity = 2
            val currentQuantity = 2
            val shopId = "5"
            val warehouseId = 3L

            val addToCartResponse = AddToCartDataModel()
            val miniCartItems = mapOf(
                MiniCartItemKey("5") to MiniCartItem.MiniCartItemProduct(
                    productId = "5",
                    quantity = currentQuantity
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetShopId_thenReturn(shopId.toLong())
            onGetWarehouseId_thenReturn(warehouseId)
            onGetUserLoggedIn_thenReturn(isLoggedIn = true)

            onAddToCart_thenReturn(addToCartResponse)
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.getMiniCart()
            viewModel.onCartQuantityChanged(productId, shopId, newQuantity, 1, false)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetMiniCartUseCaseCalled()
            verifyAddToCartUseCaseNotCalled()
            verifyUpdateCartUseCaseNotCalled()
            verifyDeleteCartUseCaseNotCalled()

            viewModel.addItemToCart.verifyValueEquals(null)
            viewModel.updateCartItem.verifyValueEquals(null)
            viewModel.removeCartItem.verifyValueEquals(null)
        }
    }

    @Test
    fun `when getMiniCart success should set miniCart value success`() {
        val shopId = 3L
        val warehouseId = 5L
        val response = MiniCartSimplifiedData()

        onGetShopId_thenReturn(shopId)
        onGetWarehouseId_thenReturn(warehouseId)

        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getMiniCart()

        verifyGetMiniCartUseCaseCalled()

        viewModel.miniCart
            .verifySuccessEquals(Success(response))
    }

    @Test
    fun `given out of coverage true when getMiniCart success should set isShowMiniCartWidget false`() {
        val shopId = 3L
        val warehouseId = 5L
        val outOfCoverage = true
        val response = MiniCartSimplifiedData(isShowMiniCartWidget = true)

        onGetShopId_thenReturn(shopId)
        onGetWarehouseId_thenReturn(warehouseId)
        onGetIsOutOfCoverage_thenReturn(outOfCoverage)

        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getMiniCart()

        verifyGetMiniCartUseCaseCalled()

        val expectedMiniCartData = MiniCartSimplifiedData(isShowMiniCartWidget = false)

        viewModel.miniCart
            .verifySuccessEquals(Success(expectedMiniCartData))
    }

    @Test
    fun `given out of coverage false when getMiniCart success should set isShowMiniCartWidget true`() {
        val shopId = 3L
        val warehouseId = 5L
        val outOfCoverage = false
        val response = MiniCartSimplifiedData(isShowMiniCartWidget = true)

        onGetShopId_thenReturn(shopId)
        onGetWarehouseId_thenReturn(warehouseId)
        onGetIsOutOfCoverage_thenReturn(outOfCoverage)

        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getMiniCart()

        verifyGetMiniCartUseCaseCalled()

        val expectedMiniCartData = MiniCartSimplifiedData(isShowMiniCartWidget = true)

        viewModel.miniCart
            .verifySuccessEquals(Success(expectedMiniCartData))
    }

    @Test
    fun `when getMiniCart twice should set miniCart value success`() {
        val shopId = 3L
        val warehouseId = 5L
        val response = MiniCartSimplifiedData()

        onGetShopId_thenReturn(shopId)
        onGetWarehouseId_thenReturn(warehouseId)

        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getMiniCart()
        viewModel.getMiniCart()

        verifyGetMiniCartUseCaseCalled()

        viewModel.miniCart
            .verifySuccessEquals(Success(response))
    }

    @Test
    fun `when getMiniCart error should set miniCart value FAIL`() {
        val shopId = 3L
        val warehouseId = 5L
        val error = NullPointerException()

        onGetShopId_thenReturn(shopId)
        onGetWarehouseId_thenReturn(warehouseId)

        onGetMiniCart_throwException(error)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getMiniCart()

        verifyGetMiniCartUseCaseCalled()

        viewModel.miniCart
            .verifyErrorEquals(Fail(error))
    }

    @Test
    fun `given shopId zero when getMiniCart should NOT call get mini cart use case`() {
        val shopId = 0L
        val warehouseId = 5L
        val response = MiniCartSimplifiedData()

        onGetShopId_thenReturn(shopId)
        onGetWarehouseId_thenReturn(warehouseId)
        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getMiniCart()

        verifyGetMiniCartUseCaseNotCalled()
    }

    @Test
    fun `given warehouseId zero when getMiniCart should NOT call get mini cart use case`() {
        val shopId = 3L
        val warehouseId = 0L
        val response = MiniCartSimplifiedData()

        onGetShopId_thenReturn(shopId)
        onGetWarehouseId_thenReturn(warehouseId)

        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.getMiniCart()

        verifyGetMiniCartUseCaseNotCalled()
    }

    @Test
    fun `given user not loggedIn when getMiniCart should NOT call get mini cart use case`() {
        val shopId = 1L
        val warehouseId = 1L
        val isLoggedIn = false
        val response = MiniCartSimplifiedData()

        onGetShopId_thenReturn(shopId)
        onGetWarehouseId_thenReturn(warehouseId)
        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn)

        viewModel.getMiniCart()

        verifyGetMiniCartUseCaseNotCalled()
    }

    @Test
    fun `given miniCartSource null when getMiniCart should not call minicart use case`() {
        viewModel.miniCartSource = null

        val shopId = 1L
        val warehouseId = 1L
        val isLoggedIn = false
        val response = MiniCartSimplifiedData()

        onGetShopId_thenReturn(shopId)
        onGetWarehouseId_thenReturn(warehouseId)
        onGetMiniCart_thenReturn(response)
        onGetUserLoggedIn_thenReturn(isLoggedIn)

        viewModel.getMiniCart()

        verifyGetMiniCartUseCaseNotCalled()
    }

    @Test
    fun `given user not logged in when onCartQuantityChanged called should update openLoginPage live data`() {
        onGetUserLoggedIn_thenReturn(isLoggedIn = false)

        viewModel.onCartQuantityChanged(
            productId = "",
            shopId = "",
            quantity = 0,
            stock = 0,
            isVariant = false
        )

        viewModel.openLoginPage.verifyValueEquals(Unit)
    }

    @Test
    fun `when getTickerDataAsync error should return null`() {
        runTest {
            onGetTickerDataAsync_thenReturn(NullPointerException())

            val actualResult = viewModel
                .getTickerDataAsync(warehouseId = "1", page = "1").await()

            assertEquals(null, actualResult)
        }
    }
}
