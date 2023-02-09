package com.tokopedia.tokopedianow.productrecommendation

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.cartcommon.data.response.deletecart.Data
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.tokopedianow.common.analytics.model.AddToCartDataTrackerModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Test

class AddToCartTest: TokoNowProductRecommendationViewModelTestFixture() {

    @Test
    fun `while adding product to cart, the request should be success`() {
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

        viewModel.addProductToCart(position, quantity, shopId)

        viewModel.miniCartAdd.verifySuccessEquals(Success(response))
        viewModel.atcDataTracker.verifyValueEquals(expectedAddToCartDataTrackerModel)
    }

    @Test
    fun `while adding product to cart, the request should fail`() {
        val position = 1
        val quantity = 2

        val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId

        mockProductModels()

        onAddToCart_thenReturn(Throwable())

        viewModel.addProductToCart(position, quantity, shopId)

        viewModel.miniCartAdd.verifyErrorEquals(Fail(Throwable()))
    }

    @Test
    fun `while updating product to cart, the request should be success`() {
        val position = 0
        val quantity = 4
        val message = "success"

        val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId
        val productId = expectedProduct.getProductId()

        mockProductModels()

        mockMiniCartSimplifiedData(productId)

        val response = UpdateCartV2Data(
            error = listOf(message),
        )

        onUpdateItemCart_thenReturn(response)

        viewModel.addProductToCart(position, quantity, shopId)

        viewModel.miniCartUpdate.verifySuccessEquals(Success(response))
    }

    @Test
    fun `while updating product to cart, the request should fail`() {
        val position = 1
        val quantity = 2

        val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId
        val productId = expectedProduct.getProductId()

        mockProductModels()

        mockMiniCartSimplifiedData(productId)

        onUpdateItemCart_thenReturn(Throwable())

        viewModel.addProductToCart(position, quantity, shopId)

        viewModel.miniCartUpdate.verifyErrorEquals(Fail(Throwable()))
    }

    @Test
    fun `while removing product to cart, the request should be success`() {
        val position = 0
        val quantity = 0
        val success = 1
        val message = "success"

        val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId
        val productId = expectedProduct.getProductId()

        mockProductModels()

        mockMiniCartSimplifiedData(productId)

        val response = RemoveFromCartData(
            errorMessage = listOf(message),
            data = Data(
                success = success,
                message = listOf(message)
            )
        )

        onRemoveItemCart_thenReturn(response)

        viewModel.addProductToCart(position, quantity, shopId)

        viewModel.miniCartRemove.verifySuccessEquals(Success(Pair(productId, response.data.message.joinToString(separator = ", "))))
    }

    @Test
    fun `while removing product to cart, the request should fail`() {
        val position = 0
        val quantity = 0

        val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId
        val productId = expectedProduct.getProductId()

        onRemoveItemCart_thenReturn(Throwable())

        mockProductModels()

        mockMiniCartSimplifiedData(productId)

        viewModel.addProductToCart(
            position = position,
            quantity = quantity,
            shopId = shopId
        )

        viewModel.miniCartRemove.verifyErrorEquals(Fail(Throwable()))
    }

    @Test
    fun `while adding product to cart if quantity is 0 and mMiniCartSimplifiedData is null, it will do nothing`() {
        val position = 1
        val quantity = 0

        val expectedProduct = productModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId

        viewModel.mockPrivateField(
            name = privateFieldProductModels,
            value = productModels
        )

        viewModel.addProductToCart(
            position = position,
            quantity = quantity,
            shopId = shopId
        )

        viewModel.miniCartAdd.verifyValueEquals(null)
        viewModel.miniCartUpdate.verifyValueEquals(null)
        viewModel.miniCartRemove.verifyValueEquals(null)
    }

    @Test
    fun `while adding product to cart if product is not TokoNowProductCardCarouselItemUiModel, it will do nothing`() {
        val position = productModels.size - 1
        val quantity = 0
        val shopId = "12221"

        viewModel.mockPrivateField(
            name = privateFieldProductModels,
            value = productModels
        )

        viewModel.addProductToCart(
            position = position,
            quantity = quantity,
            shopId = shopId
        )

        viewModel.miniCartAdd.verifyValueEquals(null)
        viewModel.miniCartUpdate.verifyValueEquals(null)
        viewModel.miniCartRemove.verifyValueEquals(null)
    }

    @Test
    fun `while adding product to cart if product list size is not more than position, it will do nothing`() {
        val position = 10
        val quantity = 0
        val shopId = "12221"

        viewModel.mockPrivateField(
            name = privateFieldProductModels,
            value = productModels
        )

        viewModel.addProductToCart(
            position = position,
            quantity = quantity,
            shopId = shopId
        )

        viewModel.miniCartAdd.verifyValueEquals(null)
        viewModel.miniCartUpdate.verifyValueEquals(null)
        viewModel.miniCartRemove.verifyValueEquals(null)
    }

}
