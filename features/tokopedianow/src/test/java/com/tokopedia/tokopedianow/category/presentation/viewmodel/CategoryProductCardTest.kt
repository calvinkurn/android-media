package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType
import com.tokopedia.tokopedianow.category.presentation.util.CategoryProductCardUtil.mapAddToCartResponse
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
    fun `while adding product to cart, the request should be success`() = runTest {
        val parentProductId = "1223"
        val productId = "2025598474"
        val productOrderQuantity = 0
        val productStock = 5
        val productName = "Jeruk"
        val productPrice = "Rp. 2000"
        val productPosition = 0
        val showcaseTitle = "Buah- Buahan"
        val showcaseId = "133333"
        val shopId = "11515028"
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

//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `while updating product to cart, the request should be success`() {
//        runTest {
//            val addToCartProductResponse = "category/update-product-in-cart.json".jsonToObject<UpdateCartGqlResponse>()
//
//            coEvery {
//                updateCartUseCase.execute(any(), any())
//            } answers {
//                firstArg<(AddToCartDataModel) -> Unit>().invoke(addToCartProductResponse.updateCartData)
//            }
//
//            val categoryShowcaseItemUiModel = CategoryShowcaseItemUiModel(
//                productCardModel = ProductCardCompactUiModel(
//                    productId = "2025598474",
//                    orderQuantity = 0,
//                    availableStock = 5,
//                    name = "product name",
//                    price = "1000"
//                ),
//                parentProductId = "1223",
//                headerName = "testing"
//            )
//
//            val categoryShowcaseUiModel = CategoryShowcaseUiModel(id = "133333", productListUiModels = listOf(categoryShowcaseItemUiModel))
//
//            viewModel.mockPrivateField("layout", mutableListOf(categoryShowcaseUiModel))
//
//            viewModel.onCartQuantityChanged(
//                productId = categoryShowcaseItemUiModel.productCardModel.productId,
//                quantity = 2,
//                stock = categoryShowcaseItemUiModel.productCardModel.availableStock,
//                shopId = "11515028",
//                position = 0,
//                isOos = categoryShowcaseItemUiModel.productCardModel.isOos(),
//                name = categoryShowcaseItemUiModel.productCardModel.name,
//                categoryIdL1 = categoryIdL1,
//                price = categoryShowcaseItemUiModel.productCardModel.price.getDigits().orZero(),
//                headerName = categoryShowcaseItemUiModel.headerName,
//                layoutType = CategoryLayoutType.CATEGORY_SHOWCASE,
//            )
//
//            viewModel.addItemToCart.getOrAwaitValue()
//            viewModel.addItemToCart.verifyValueEquals(Success(addToCartDataModel))
//
//            viewModel.categoryPage.getOrAwaitValue()
//            viewModel.categoryPage.verifyValueEquals(listOf(categoryShowcaseUiModel.copy(productListUiModels = listOf(categoryShowcaseItemUiModel.copy(productCardModel = categoryShowcaseItemUiModel.productCardModel.copy(orderQuantity = 2))))))
//        }
//    }
}
