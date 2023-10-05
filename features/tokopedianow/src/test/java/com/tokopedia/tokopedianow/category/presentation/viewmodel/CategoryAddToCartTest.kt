package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.category.presentation.util.AddToCartMapper.mapAddToCartResponse
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryAddToCartTest : TokoNowCategoryMainViewModelTestFixture() {

    @Test
    fun `when adding product to cart, request should be successful`() = runTest {
        // mock data
        val parentProductId = "1223"
        val productId = "2025598474"
        val productOrderQuantity = 0
        val productStock = 5
        val productName = "Jeruk"
        val productPrice = "Rp. 2000"
        val productPosition = 0
        val showcaseTitle = "Buah- Buahan"
        val showcaseId = "133333"
        val privateFieldNameLayout = "layout"
        val newProductOrderQuantity = 2

        val addToCartDataModel = mapAddToCartResponse(addToCartGqlResponse)
        onAddToCart_thenReturns(addToCartDataModel)

        val categoryShowcaseItemUiModel = CategoryShowcaseItemUiModel(
            productCardModel = ProductCardCompactUiModel(
                productId = productId,
                orderQuantity = productOrderQuantity,
                availableStock = productStock,
                name = productName,
                price = productPrice
            ),
            parentProductId = parentProductId,
            headerName = showcaseTitle
        )

        val categoryShowcaseUiModel = CategoryShowcaseUiModel(
            id = showcaseId,
            title = showcaseTitle,
            productListUiModels = listOf(
                categoryShowcaseItemUiModel
            )
        )

        val mockLayout = mutableListOf(categoryShowcaseUiModel)
        viewModel.mockPrivateField(
            name = privateFieldNameLayout,
            value = mockLayout
        )

        // change product quantity
        viewModel.onCartQuantityChanged(
            product = categoryShowcaseItemUiModel.productCardModel,
            shopId = categoryShowcaseItemUiModel.shopId,
            quantity = newProductOrderQuantity,
            layoutType = CategoryLayoutType.CATEGORY_SHOWCASE.name
        )

        // verify data
        viewModel.addItemToCart
            .getOrAwaitValue()
        viewModel.addItemToCart
            .verifyValueEquals(Success(addToCartDataModel))

        viewModel.updateToolbarNotification
            .getOrAwaitValue()
        viewModel.updateToolbarNotification
            .verifyValueEquals(true)

        viewModel.atcDataTracker
            .getOrAwaitValue()
        viewModel.atcDataTracker
            .verifyValueEquals(
                CategoryAtcTrackerModel(
                    categoryIdL1 = categoryIdL1,
                    index = productPosition,
                    headerName = categoryShowcaseItemUiModel.headerName,
                    quantity = newProductOrderQuantity,
                    product = categoryShowcaseItemUiModel.productCardModel,
                    layoutType = CategoryLayoutType.CATEGORY_SHOWCASE.name
                )
            )

        viewModel.categoryPage
            .getOrAwaitValue()
        viewModel.categoryPage
            .verifyValueEquals(
                mockLayout.map {
                    it.copy(
                        productListUiModels = listOf(
                            categoryShowcaseItemUiModel.copy(
                                productCardModel = categoryShowcaseItemUiModel.productCardModel.copy(
                                    orderQuantity = newProductOrderQuantity
                                )
                            )
                        )
                    )
                }
            )
    }

    @Test
    fun `when updating product to cart, request should be successful`() = runTest {
        // mock data
        val parentProductId = "1223"
        val productId = "2025598474"
        val productOrderQuantity = 1
        val productStock = 5
        val productName = "Jeruk"
        val productPrice = "Rp. 2000"
        val showcaseTitle = "Buah- Buahan"
        val showcaseId = "133333"
        val privateFieldNameLayout = "layout"
        val newProductOrderQuantity = 2
        val isLoggedIn = true
        val userId = "14445"
        val deviceId = "88889"

        onUserSession_thenReturns(
            isLoggedIn = isLoggedIn,
            userId = userId,
            deviceId = deviceId
        )
        onUpdateProductInCart_thenReturns()
        onGetMiniCart_thenReturns()

        val categoryShowcaseItemUiModel = CategoryShowcaseItemUiModel(
            productCardModel = ProductCardCompactUiModel(
                productId = productId,
                orderQuantity = productOrderQuantity,
                availableStock = productStock,
                name = productName,
                price = productPrice
            ),
            parentProductId = parentProductId,
            headerName = showcaseTitle
        )

        val categoryShowcaseUiModel = CategoryShowcaseUiModel(
            id = showcaseId,
            title = showcaseTitle,
            productListUiModels = listOf(
                categoryShowcaseItemUiModel
            )
        )

        val mockLayout = mutableListOf(categoryShowcaseUiModel)
        viewModel.mockPrivateField(
            name = privateFieldNameLayout,
            value = mockLayout
        )

        // get minicart
        viewModel.getMiniCart()

        // change product quantity
        viewModel.onCartQuantityChanged(
            product = categoryShowcaseItemUiModel.productCardModel,
            shopId = categoryShowcaseItemUiModel.shopId,
            quantity = newProductOrderQuantity,
            layoutType = CategoryLayoutType.CATEGORY_SHOWCASE.name
        )

        viewModel.updateToolbarNotification
            .getOrAwaitValue()
        viewModel.updateToolbarNotification
            .verifyValueEquals(true)

        // verify data
        viewModel.updateCartItem
            .getOrAwaitValue()
        viewModel.updateCartItem
            .verifyValueEquals(
                Success(
                    Triple(
                        productId,
                        updateProductInCartResponse.updateCartData,
                        newProductOrderQuantity
                    )
                )
            )

        viewModel.categoryPage
            .getOrAwaitValue()
        viewModel.categoryPage
            .verifyValueEquals(
                mockLayout.map {
                    it.copy(
                        productListUiModels = listOf(
                            categoryShowcaseItemUiModel.copy(
                                productCardModel = categoryShowcaseItemUiModel.productCardModel.copy(
                                    orderQuantity = newProductOrderQuantity
                                )
                            )
                        )
                    )
                }
            )
    }

    @Test
    fun `when removing product to cart, request should be successful`() = runTest {
        // mock data
        val parentProductId = "1223"
        val productId = "2025598474"
        val productOrderQuantity = 1
        val productStock = 5
        val productName = "Jeruk"
        val productPrice = "Rp. 2000"
        val showcaseTitle = "Buah- Buahan"
        val showcaseId = "133333"
        val privateFieldNameLayout = "layout"
        val newProductOrderQuantity = 0
        val isLoggedIn = true
        val userId = "14445"
        val deviceId = "88889"

        onUserSession_thenReturns(
            isLoggedIn = isLoggedIn,
            userId = userId,
            deviceId = deviceId
        )
        onRemoveProductFromCart_thenReturns()
        onGetMiniCart_thenReturns()

        val categoryShowcaseItemUiModel = CategoryShowcaseItemUiModel(
            productCardModel = ProductCardCompactUiModel(
                productId = productId,
                orderQuantity = productOrderQuantity,
                availableStock = productStock,
                name = productName,
                price = productPrice
            ),
            parentProductId = parentProductId,
            headerName = showcaseTitle
        )

        val categoryShowcaseUiModel = CategoryShowcaseUiModel(
            id = showcaseId,
            title = showcaseTitle,
            productListUiModels = listOf(
                categoryShowcaseItemUiModel
            )
        )

        val mockLayout = mutableListOf(categoryShowcaseUiModel)
        viewModel.mockPrivateField(
            name = privateFieldNameLayout,
            value = mockLayout
        )

        // get minicart
        viewModel.getMiniCart()

        // change product quantity
        viewModel.onCartQuantityChanged(
            product = categoryShowcaseItemUiModel.productCardModel,
            shopId = categoryShowcaseItemUiModel.shopId,
            quantity = newProductOrderQuantity,
            layoutType = CategoryLayoutType.CATEGORY_SHOWCASE.name
        )

        viewModel.updateToolbarNotification
            .getOrAwaitValue()
        viewModel.updateToolbarNotification
            .verifyValueEquals(true)

        // verify data
        viewModel.removeCartItem
            .getOrAwaitValue()
        viewModel.removeCartItem
            .verifyValueEquals(
                Success(
                    Pair(
                        productId,
                        removeProductFromCartResponse.data.message.joinToString(separator = ", ")
                    )
                )
            )

        viewModel.categoryPage
            .getOrAwaitValue()
        viewModel.categoryPage
            .verifyValueEquals(
                mockLayout.map {
                    it.copy(
                        productListUiModels = listOf(
                            categoryShowcaseItemUiModel.copy(
                                productCardModel = categoryShowcaseItemUiModel.productCardModel.copy(
                                    orderQuantity = newProductOrderQuantity
                                )
                            )
                        )
                    )
                }
            )
    }

    @Test
    fun `when adding product to cart, request should be failed`() = runTest {
        // mock data
        val parentProductId = "1223"
        val productId = "2025598474"
        val productOrderQuantity = 0
        val productStock = 5
        val productName = "Jeruk"
        val productPrice = "Rp. 2000"
        val showcaseTitle = "Buah- Buahan"
        val showcaseId = "133333"
        val privateFieldNameLayout = "layout"
        val newProductOrderQuantity = 2

        val exception = Exception()
        onAddToCart_thenThrows(exception)

        val categoryShowcaseItemUiModel = CategoryShowcaseItemUiModel(
            productCardModel = ProductCardCompactUiModel(
                productId = productId,
                orderQuantity = productOrderQuantity,
                availableStock = productStock,
                name = productName,
                price = productPrice
            ),
            parentProductId = parentProductId,
            headerName = showcaseTitle
        )

        val categoryShowcaseUiModel = CategoryShowcaseUiModel(
            id = showcaseId,
            title = showcaseTitle,
            productListUiModels = listOf(
                categoryShowcaseItemUiModel
            )
        )

        val mockLayout = mutableListOf(categoryShowcaseUiModel)
        viewModel.mockPrivateField(
            name = privateFieldNameLayout,
            value = mockLayout
        )

        // change product quantity
        viewModel.onCartQuantityChanged(
            product = categoryShowcaseItemUiModel.productCardModel,
            shopId = categoryShowcaseItemUiModel.shopId,
            quantity = newProductOrderQuantity,
            layoutType = CategoryLayoutType.CATEGORY_SHOWCASE.name
        )

        // verify data
        viewModel.addItemToCart
            .getOrAwaitValue()
        viewModel.addItemToCart
            .verifyValueEquals(Fail(exception))

        viewModel.categoryPage
            .getOrAwaitValue()
        viewModel.categoryPage
            .verifyValueEquals(
                mockLayout.map {
                    it.copy(
                        productListUiModels = listOf(
                            categoryShowcaseItemUiModel.copy(
                                productCardModel = categoryShowcaseItemUiModel.productCardModel.copy(
                                    orderQuantity = newProductOrderQuantity
                                )
                            )
                        )
                    )
                }
            )
    }

    @Test
    fun `when updating product to cart, request should be failed`() = runTest {
        // mock data
        val parentProductId = "1223"
        val productId = "2025598474"
        val productOrderQuantity = 1
        val productStock = 5
        val productName = "Jeruk"
        val productPrice = "Rp. 2000"
        val showcaseTitle = "Buah- Buahan"
        val showcaseId = "133333"
        val privateFieldNameLayout = "layout"
        val newProductOrderQuantity = 2
        val isLoggedIn = true
        val userId = "14445"
        val deviceId = "88889"

        onUserSession_thenReturns(
            isLoggedIn = isLoggedIn,
            userId = userId,
            deviceId = deviceId
        )
        val exception = Exception()
        onUpdateProductInCart_thenThrows(exception)
        onGetMiniCart_thenReturns()

        val categoryShowcaseItemUiModel = CategoryShowcaseItemUiModel(
            productCardModel = ProductCardCompactUiModel(
                productId = productId,
                orderQuantity = productOrderQuantity,
                availableStock = productStock,
                name = productName,
                price = productPrice
            ),
            parentProductId = parentProductId,
            headerName = showcaseTitle
        )

        val categoryShowcaseUiModel = CategoryShowcaseUiModel(
            id = showcaseId,
            title = showcaseTitle,
            productListUiModels = listOf(
                categoryShowcaseItemUiModel
            )
        )

        val mockLayout = mutableListOf(categoryShowcaseUiModel)
        viewModel.mockPrivateField(
            name = privateFieldNameLayout,
            value = mockLayout
        )

        // get minicart
        viewModel.getMiniCart()

        // change product quantity
        viewModel.onCartQuantityChanged(
            product = categoryShowcaseItemUiModel.productCardModel,
            shopId = categoryShowcaseItemUiModel.shopId,
            quantity = newProductOrderQuantity,
            layoutType = CategoryLayoutType.CATEGORY_SHOWCASE.name
        )

        // verify data
        viewModel.updateCartItem
            .getOrAwaitValue()
        viewModel.updateCartItem
            .verifyValueEquals(Fail(exception))

        viewModel.categoryPage
            .getOrAwaitValue()
        viewModel.categoryPage
            .verifyValueEquals(
                mockLayout.map {
                    it.copy(
                        productListUiModels = listOf(
                            categoryShowcaseItemUiModel.copy(
                                productCardModel = categoryShowcaseItemUiModel.productCardModel.copy(
                                    orderQuantity = newProductOrderQuantity
                                )
                            )
                        )
                    )
                }
            )
    }

    @Test
    fun `when removing product to cart, request should be failed`() = runTest {
        // mock data
        val parentProductId = "1223"
        val productId = "2025598474"
        val productOrderQuantity = 1
        val productStock = 5
        val productName = "Jeruk"
        val productPrice = "Rp. 2000"
        val showcaseTitle = "Buah- Buahan"
        val showcaseId = "133333"
        val privateFieldNameLayout = "layout"
        val newProductOrderQuantity = 0
        val isLoggedIn = true
        val userId = "14445"
        val deviceId = "88889"

        onUserSession_thenReturns(
            isLoggedIn = isLoggedIn,
            userId = userId,
            deviceId = deviceId
        )
        val exception = Exception()
        onRemoveProductFromCart_thenThrows(exception)
        onGetMiniCart_thenReturns()

        val categoryShowcaseItemUiModel = CategoryShowcaseItemUiModel(
            productCardModel = ProductCardCompactUiModel(
                productId = productId,
                orderQuantity = productOrderQuantity,
                availableStock = productStock,
                name = productName,
                price = productPrice
            ),
            parentProductId = parentProductId,
            headerName = showcaseTitle
        )

        val categoryShowcaseUiModel = CategoryShowcaseUiModel(
            id = showcaseId,
            title = showcaseTitle,
            productListUiModels = listOf(
                categoryShowcaseItemUiModel
            )
        )

        val mockLayout = mutableListOf(categoryShowcaseUiModel)
        viewModel.mockPrivateField(
            name = privateFieldNameLayout,
            value = mockLayout
        )

        // get minicart
        viewModel.getMiniCart()

        // change product quantity
        viewModel.onCartQuantityChanged(
            product = categoryShowcaseItemUiModel.productCardModel,
            shopId = categoryShowcaseItemUiModel.shopId,
            quantity = newProductOrderQuantity,
            layoutType = CategoryLayoutType.CATEGORY_SHOWCASE.name
        )

        // verify data
        viewModel.removeCartItem
            .getOrAwaitValue()
        viewModel.removeCartItem
            .verifyValueEquals(Fail(exception))

        viewModel.categoryPage
            .getOrAwaitValue()
        viewModel.categoryPage
            .verifyValueEquals(
                mockLayout.map {
                    it.copy(
                        productListUiModels = listOf(
                            categoryShowcaseItemUiModel.copy(
                                productCardModel = categoryShowcaseItemUiModel.productCardModel.copy(
                                    orderQuantity = newProductOrderQuantity
                                )
                            )
                        )
                    )
                }
            )
    }
}
