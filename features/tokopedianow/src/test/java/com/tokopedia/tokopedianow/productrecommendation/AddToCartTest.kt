package com.tokopedia.tokopedianow.productrecommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.cartcommon.data.response.deletecart.Data
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Test

class AddToCartTest: TokoNowProductRecommendationViewModelTestFixture() {

    private val mockProductModels = mutableListOf<Visitable<*>>(
        TokoNowProductCardCarouselItemUiModel(
            productCardModel = TokoNowProductCardViewUiModel(
                productId = "11111",
                name = "product a",
                price = "RP. 10.000",
                minOrder = 1,
                orderQuantity = 2,
                availableStock = 100,
                maxOrder = 6
            ),
            shopId = "122212"
        ),
        TokoNowProductCardCarouselItemUiModel(
            productCardModel = TokoNowProductCardViewUiModel(
                productId = "11112",
                name = "product b",
                price = "RP. 30.000",
                minOrder = 1,
                orderQuantity = 0,
                availableStock = 100,
                maxOrder = 3
            ),
            shopId = "122212"
        ),
        TokoNowProductCardCarouselItemUiModel(
            productCardModel = TokoNowProductCardViewUiModel(
                productId = "11113",
                name = "product c",
                price = "RP. 20.000",
                minOrder = 2,
                orderQuantity = 1,
                availableStock = 90,
                maxOrder = 2
            ),
            shopId = "122212"
        ),
        TokoNowSeeMoreCardCarouselUiModel()
    )

    @Test
    fun `while adding product to cart, the request should be success`() {
        val position = 1
        val quantity = 2
        val cartId = "22122121"
        val success = 1

        val privateFieldName = "productModels"
        val expectedProduct = mockProductModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId
        val productId = expectedProduct.getProductId()

        viewModel.mockPrivateField(
            name = privateFieldName,
            value = mockProductModels
        )

        val response = AddToCartDataModel(
            data = DataModel(
                success = success,
                cartId = cartId,
                productId = productId.toLongOrZero(),
                shopId = shopId.toLongOrZero(),
                quantity = quantity
            )
        )

        onAddToCart_thenReturn(response)

        viewModel.addProductToCart(position, quantity, shopId)

        viewModel.miniCartAdd.verifySuccessEquals(Success(response))
    }

    @Test
    fun `while adding product to cart, the request should fail`() {
        val position = 1
        val quantity = 2

        val privateFieldName = "productModels"
        val expectedProduct = mockProductModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId

        viewModel.mockPrivateField(
            name = privateFieldName,
            value = mockProductModels
        )

        onAddToCart_thenReturn(Throwable())

        viewModel.addProductToCart(position, quantity, shopId)

        viewModel.miniCartAdd.verifyErrorEquals(Fail(Throwable()))
    }

    @Test
    fun `while updating product to cart, the request should be success`() {
        val position = 0
        val quantity = 4
        val cartId = "22122121"
        val success = 1

        val privateFieldName = "productModels"
        val expectedProduct = mockProductModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId
        val productId = expectedProduct.getProductId()

        viewModel.mockPrivateField(
            name = privateFieldName,
            value = mockProductModels
        )

        viewModel.mockPrivateField(
            name = "mMiniCartSimplifiedData",
            value = MiniCartSimplifiedData(
                    miniCartItems = mapOf(Pair(
                        MiniCartItemKey(id = productId),
                        MiniCartItem.MiniCartItemProduct()
                    )
                )
            )
        )

        val response = UpdateCartV2Data(
            error = listOf("success"),
        )

        onUpdateItemCart_thenReturn(response)

        viewModel.addProductToCart(position, quantity, shopId)

        viewModel.miniCartUpdate.verifySuccessEquals(Success(response))
    }

    @Test
    fun `while updating product to cart, the request should fail`() {
        val position = 1
        val quantity = 2

        val privateFieldName = "productModels"
        val expectedProduct = mockProductModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId
        val productId = expectedProduct.getProductId()

        viewModel.mockPrivateField(
            name = privateFieldName,
            value = mockProductModels
        )

        viewModel.mockPrivateField(
            name = "mMiniCartSimplifiedData",
            value = MiniCartSimplifiedData(
                miniCartItems = mapOf(Pair(
                    MiniCartItemKey(id = productId),
                    MiniCartItem.MiniCartItemProduct()
                )
                )
            )
        )

        onUpdateItemCart_thenReturn(Throwable())

        viewModel.addProductToCart(position, quantity, shopId)

        viewModel.miniCartUpdate.verifyErrorEquals(Fail(Throwable()))
    }

    @Test
    fun `while removing product to cart, the request should be success`() {
        val position = 0
        val quantity = 0
        val cartId = "22122121"
        val success = 1

        val privateFieldName = "productModels"
        val expectedProduct = mockProductModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId
        val productId = expectedProduct.getProductId()

        viewModel.mockPrivateField(
            name = privateFieldName,
            value = mockProductModels
        )

        viewModel.mockPrivateField(
            name = "mMiniCartSimplifiedData",
            value = MiniCartSimplifiedData(
                miniCartItems = mapOf(Pair(
                    MiniCartItemKey(id = productId),
                    MiniCartItem.MiniCartItemProduct(productId = productId)
                )
                )
            )
        )

        val response = RemoveFromCartData(
            errorMessage = listOf("success"),
            data = Data(
                success = 1,
                message = listOf("success")
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

        val privateFieldName = "productModels"
        val expectedProduct = mockProductModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId
        val productId = expectedProduct.getProductId()

        viewModel.mockPrivateField(
            name = privateFieldName,
            value = mockProductModels
        )

        viewModel.mockPrivateField(
            name = "mMiniCartSimplifiedData",
            value = MiniCartSimplifiedData(
                miniCartItems = mapOf(Pair(
                    MiniCartItemKey(id = productId),
                    MiniCartItem.MiniCartItemProduct(productId = productId)
                )
                )
            )
        )

        onRemoveItemCart_thenReturn(Throwable())

        viewModel.addProductToCart(position, quantity, shopId)

        viewModel.miniCartRemove.verifyErrorEquals(Fail(Throwable()))
    }

    @Test
    fun `while adding product to cart if quantity is 0 and mMiniCartSimplifiedData is null, it will do nothing`() {
        val position = 1
        val quantity = 0

        val expectedProduct = mockProductModels[position] as TokoNowProductCardCarouselItemUiModel
        val shopId = expectedProduct.shopId

        val privateFieldName = "productModels"
        viewModel.mockPrivateField(
            name = privateFieldName,
            value = mockProductModels
        )

        viewModel.addProductToCart(position, quantity, shopId)

        viewModel.miniCartAdd.verifyValueEquals(null)
        viewModel.miniCartUpdate.verifyValueEquals(null)
        viewModel.miniCartRemove.verifyValueEquals(null)
    }

    @Test
    fun `while adding product to cart if product is not TokoNowProductCardCarouselItemUiModel, it will do nothing`() {
        val position = mockProductModels.size - 1
        val quantity = 0
        val shopId = "12221"

        val privateFieldName = "productModels"
        viewModel.mockPrivateField(
            name = privateFieldName,
            value = mockProductModels
        )

        viewModel.addProductToCart(position, quantity, shopId)

        viewModel.miniCartAdd.verifyValueEquals(null)
        viewModel.miniCartUpdate.verifyValueEquals(null)
        viewModel.miniCartRemove.verifyValueEquals(null)
    }

    @Test
    fun `while adding product to cart if product list size is not more than position, it will do nothing`() {
        val position = 10
        val quantity = 0
        val shopId = "12221"

        val privateFieldName = "productModels"
        viewModel.mockPrivateField(
            name = privateFieldName,
            value = mockProductModels
        )

        viewModel.addProductToCart(position, quantity, shopId)

        viewModel.miniCartAdd.verifyValueEquals(null)
        viewModel.miniCartUpdate.verifyValueEquals(null)
        viewModel.miniCartRemove.verifyValueEquals(null)
    }

}
