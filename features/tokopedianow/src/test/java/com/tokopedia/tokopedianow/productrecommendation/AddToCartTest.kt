package com.tokopedia.tokopedianow.productrecommendation

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.cartcommon.data.response.deletecart.Data
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.tokopedianow.common.analytics.model.AddToCartDataTrackerModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class AddToCartTest : TokoNowProductRecommendationViewModelTestFixture() {

    companion object {
        private const val CHANGE_QUANTITY_DELAY = 500L
    }

    @Test
    fun `while adding product to cart, the request should be success`() {
        coroutineTestRule.runBlockingTest {
            val position = 1
            val quantity = 2
            val cartId = "22122121"
            val success = 1

            val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
            val shopId = expectedProduct.shopId
            val productId = expectedProduct.getProductId()

            val expectedAddToCartDataTrackerModel = AddToCartDataTrackerModel(
                position = position,
                quantity = quantity,
                cartId = cartId,
                productRecommendation = expectedProduct
            )

            mockProductModels()

            val response = AddToCartDataModel(
                data = DataModel(
                    success = success,
                    cartId = cartId,
                    productId = productId,
                    shopId = shopId,
                    quantity = quantity
                )
            )

            onAddToCart_thenReturn(response)

            viewModel.onCartQuantityChanged(position, quantity, shopId)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.addItemToCart.verifySuccessEquals(Success(response))
            viewModel.atcDataTracker.verifyValueEquals(expectedAddToCartDataTrackerModel)
        }
    }

    @Test
    fun `while adding product to cart, the request should fail`() {
        coroutineTestRule.runBlockingTest {
            val position = 1
            val quantity = 2

            val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
            val shopId = expectedProduct.shopId

            mockProductModels()

            onAddToCart_thenReturn(Throwable())

            viewModel.onCartQuantityChanged(position, quantity, shopId)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.addItemToCart.verifyErrorEquals(Fail(Throwable()))
        }
    }

    @Test
    fun `while updating product to cart, the request should be success`() {
        coroutineTestRule.runBlockingTest {
            val position = 0
            val quantity = 4
            val message = "success"

            val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
            val shopId = expectedProduct.shopId
            val productId = expectedProduct.getProductId()

            mockProductModels()

            mockMiniCartSimplifiedData(productId = productId, quantity = 1)

            val response = UpdateCartV2Data(
                error = listOf(message)
            )

            onUpdateItemCart_thenReturn(response)

            viewModel.onCartQuantityChanged(position, quantity, shopId)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.updateCartItem.verifySuccessEquals(Success(Triple(productId, response, quantity)))
        }
    }

    @Test
    fun `while updating product to cart, the request should fail`() {
        coroutineTestRule.runBlockingTest {
            val position = 1
            val quantity = 2

            val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
            val shopId = expectedProduct.shopId
            val productId = expectedProduct.getProductId()

            mockProductModels()

            mockMiniCartSimplifiedData(productId = productId, quantity = 1)

            onUpdateItemCart_thenReturn(Throwable())

            viewModel.onCartQuantityChanged(position, quantity, shopId)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.updateCartItem.verifyErrorEquals(Fail(Throwable()))
        }
    }

    @Test
    fun `while removing product to cart, the request should be success`() {
        coroutineTestRule.runBlockingTest {
            val position = 0
            val quantity = 0
            val currentQuantity = 1
            val success = 1
            val message = "success"

            val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
            val shopId = expectedProduct.shopId
            val productId = expectedProduct.getProductId()

            mockProductModels()

            mockMiniCartSimplifiedData(
                productId = productId,
                quantity = currentQuantity
            )

            val response = RemoveFromCartData(
                errorMessage = listOf(message),
                data = Data(
                    success = success,
                    message = listOf(message)
                )
            )

            onRemoveItemCart_thenReturn(response)

            viewModel.onCartQuantityChanged(position, quantity, shopId)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.removeCartItem.verifySuccessEquals(Success(Pair(productId, response.data.message.joinToString(separator = ", "))))
        }
    }

    @Test
    fun `while removing product to cart, the request should fail`() {
        coroutineTestRule.runBlockingTest {
            val position = 0
            val quantity = 0
            val currentQuantity = 1

            val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
            val shopId = expectedProduct.shopId
            val productId = expectedProduct.getProductId()

            onRemoveItemCart_thenReturn(Throwable())

            mockProductModels()

            mockMiniCartSimplifiedData(
                productId = productId,
                quantity = currentQuantity
            )

            viewModel.onCartQuantityChanged(
                position = position,
                quantity = quantity,
                shopId = shopId
            )
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.removeCartItem.verifyErrorEquals(Fail(Throwable()))
        }
    }

    @Test
    fun `while adding product to cart if quantity is 0 and mMiniCartSimplifiedData is null, it will do nothing`() {
        coroutineTestRule.runBlockingTest {
            val position = 1
            val quantity = 0

            val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
            val shopId = expectedProduct.shopId

            viewModel.mockPrivateField(
                name = privateFieldProductModels,
                value = productModels
            )

            viewModel.onCartQuantityChanged(
                position = position,
                quantity = quantity,
                shopId = shopId
            )
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.addItemToCart.verifyValueEquals(null)
            viewModel.updateCartItem.verifyValueEquals(null)
            viewModel.removeCartItem.verifyValueEquals(null)
        }
    }

    @Test
    fun `while adding product to cart if product is not TokoNowProductCardCarouselItemUiModel, it will do nothing`() {
        coroutineTestRule.runBlockingTest {
            val position = productModels.size - 1
            val quantity = 0
            val shopId = "12221"

            viewModel.mockPrivateField(
                name = privateFieldProductModels,
                value = productModels
            )

            viewModel.onCartQuantityChanged(
                position = position,
                quantity = quantity,
                shopId = shopId
            )
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.addItemToCart.verifyValueEquals(null)
            viewModel.updateCartItem.verifyValueEquals(null)
            viewModel.removeCartItem.verifyValueEquals(null)
        }
    }

    @Test
    fun `while adding product to cart if product list size is not more than position, it will do nothing`() {
        coroutineTestRule.runBlockingTest {
            val position = 10
            val quantity = 0
            val shopId = "12221"

            viewModel.mockPrivateField(
                name = privateFieldProductModels,
                value = productModels
            )

            viewModel.onCartQuantityChanged(
                position = position,
                quantity = quantity,
                shopId = shopId
            )
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.addItemToCart.verifyValueEquals(null)
            viewModel.updateCartItem.verifyValueEquals(null)
            viewModel.removeCartItem.verifyValueEquals(null)
        }
    }

    @Test
    fun `given new quantity same as cart quantity when update cart item should do nothing`() {
        val position = 0
        val quantity = 4
        val cartQuantity = 4

        val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId
        val productId = expectedProduct.getProductId()

        mockProductModels()

        mockMiniCartSimplifiedData(
            productId = productId,
            quantity = cartQuantity
        )

        val response = UpdateCartV2Data()

        onUpdateItemCart_thenReturn(response)

        viewModel.onCartQuantityChanged(productId, shopId, quantity, 1, false)

        viewModel.addItemToCart.verifyValueEquals(null)
        viewModel.updateCartItem.verifyValueEquals(null)
        viewModel.removeCartItem.verifyValueEquals(null)
    }

    @Test
    fun `given new quantity is 0 when update cart item should do nothing`() {
        val position = 0
        val quantity = 0
        val cartQuantity = 4

        val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId
        val productId = expectedProduct.getProductId()

        mockProductModels()

        mockMiniCartSimplifiedData(
            productId = productId,
            quantity = cartQuantity
        )

        val response = UpdateCartV2Data()

        onUpdateItemCart_thenReturn(response)

        viewModel.onCartQuantityChanged(productId, shopId, quantity, 1, false)

        viewModel.addItemToCart.verifyValueEquals(null)
        viewModel.updateCartItem.verifyValueEquals(null)
        viewModel.removeCartItem.verifyValueEquals(null)
    }

    @Test
    fun `given new quantity is 0 when add cart item should do nothing`() {
        val position = 0
        val quantity = 0

        val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
        val productId = expectedProduct.getProductId()
        val shopId = expectedProduct.shopId

        mockProductModels()

        val response = AddToCartDataModel()

        onAddToCart_thenReturn(response)

        viewModel.onCartQuantityChanged(productId, shopId, quantity, 1, false)

        viewModel.addItemToCart.verifyValueEquals(null)
        viewModel.updateCartItem.verifyValueEquals(null)
        viewModel.removeCartItem.verifyValueEquals(null)
    }
}
