package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType
import com.tokopedia.tokopedianow.category.presentation.util.AddToCartMapper.mapAddToCartResponse
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryProductCardTest: TokoNowCategoryMainViewModelTestFixture(){
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
            productId = categoryShowcaseItemUiModel.productCardModel.productId,
            quantity = newProductOrderQuantity,
            stock = categoryShowcaseItemUiModel.productCardModel.availableStock,
            shopId = shopId,
            position = productPosition,
            isOos = categoryShowcaseItemUiModel.productCardModel.isOos(),
            name = categoryShowcaseItemUiModel.productCardModel.name,
            categoryIdL1 = categoryIdL1,
            price = categoryShowcaseItemUiModel.productCardModel.price.getDigits().orZero(),
            headerName = categoryShowcaseItemUiModel.headerName,
            layoutType = CategoryLayoutType.CATEGORY_SHOWCASE,
        )

        //verify data
        viewModel.addItemToCart
            .getOrAwaitValue()
        viewModel.addItemToCart
            .verifyValueEquals(Success(addToCartDataModel))

        viewModel.updateToolbarNotification
            .getOrAwaitValue()
        viewModel.updateToolbarNotification
            .verifyValueEquals(true)

        val resultDataTracker = CategoryAtcTrackerModel(
            categoryIdL1 = categoryIdL1,
            index = productPosition,
            productId = categoryShowcaseItemUiModel.productCardModel.productId,
            warehouseId = warehouseId,
            isOos = categoryShowcaseItemUiModel.productCardModel.isOos(),
            name = categoryShowcaseItemUiModel.productCardModel.name,
            price = categoryShowcaseItemUiModel.productCardModel.price.getDigits().orZero(),
            headerName = categoryShowcaseItemUiModel.headerName,
            quantity = newProductOrderQuantity
        )
        viewModel.atcDataTracker
            .getOrAwaitValue()
        viewModel.atcDataTracker
            .verifyValueEquals(resultDataTracker)

        val resultCategoryPage = mockLayout.map { it.copy(productListUiModels = listOf(categoryShowcaseItemUiModel.copy(productCardModel = categoryShowcaseItemUiModel.productCardModel.copy(orderQuantity = newProductOrderQuantity)))) }
        viewModel.categoryPage
            .getOrAwaitValue()
        viewModel.categoryPage
            .verifyValueEquals(resultCategoryPage)
    }

    @Test
    fun `when updating product to cart, request should be successful`() {
        runTest {
            // mock data
            val parentProductId = "1223"
            val productId = "2025598474"
            val productOrderQuantity = 1
            val productStock = 5
            val productName = "Jeruk"
            val productPrice = "Rp. 2000"
            val productPosition = 0
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
                productId = categoryShowcaseItemUiModel.productCardModel.productId,
                quantity = newProductOrderQuantity,
                stock = categoryShowcaseItemUiModel.productCardModel.availableStock,
                shopId = shopId,
                position = productPosition,
                isOos = categoryShowcaseItemUiModel.productCardModel.isOos(),
                name = categoryShowcaseItemUiModel.productCardModel.name,
                categoryIdL1 = categoryIdL1,
                price = categoryShowcaseItemUiModel.productCardModel.price.getDigits().orZero(),
                headerName = categoryShowcaseItemUiModel.headerName,
                layoutType = CategoryLayoutType.CATEGORY_SHOWCASE,
            )

            //verify data
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
        }
    }
}
