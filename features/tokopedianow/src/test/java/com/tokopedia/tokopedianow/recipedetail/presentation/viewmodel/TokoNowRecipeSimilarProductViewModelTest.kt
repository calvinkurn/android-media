package com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel

import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.unit.test.ext.verifyValueEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowRecipeSimilarProductViewModelTest : TokoNowRecipeSimilarProductViewModelTestFixture() {

    @Test
    fun `given recipe list when onViewCreated should map atc quantity to recipe list`() {
        val miniCartItems = mapOf(
            MiniCartItemKey("2148241524") to MiniCartItem.MiniCartItemProduct(
                productId = "2148241524",
                quantity = 100
            )
        )
        val miniCartSimplifiedData = MiniCartSimplifiedData(miniCartItems = miniCartItems)
        val productList = listOf(
            RecipeProductUiModel(
                id = "2148241523",
                shopId = "480552",
                name = "kaos testing 112",
                quantity = 5,
                stock = 7,
                minOrder = 1,
                maxOrder = 7,
                priceFmt = "Rp150",
                weight = "500 g",
                imageUrl = "https://images.tokopedia.net/img/cache/250-square/hDjmkQ/2022/1/17/4524771c-4b31-4eb1-9491-0adb581431b1.jpg",
                slashedPrice = "Rp200",
                discountPercentage = "20",
                similarProducts = emptyList(),
                categoryId = "983",
                position = 1
            ),
            RecipeProductUiModel(
                id = "2148241524",
                shopId = "480552",
                name = "kaos testing 113",
                quantity = 3,
                stock = 7,
                minOrder = 1,
                maxOrder = 7,
                priceFmt = "Rp150",
                weight = "500 g",
                imageUrl = "https://images.tokopedia.net/img/cache/250-square/hDjmkQ/2022/1/17/4524771c-4b31-4eb1-9491-0adb581431b1.jpg",
                slashedPrice = "Rp200",
                discountPercentage = "20",
                similarProducts = emptyList(),
                categoryId = "983",
                position = 2
            )
        )

        viewModel.setMiniCartData(miniCartSimplifiedData)
        viewModel.onViewCreated(productList)

        val expectedProductList = listOf(
            RecipeProductUiModel(
                id = "2148241523",
                shopId = "480552",
                name = "kaos testing 112",
                quantity = 0,
                stock = 7,
                minOrder = 1,
                maxOrder = 7,
                priceFmt = "Rp150",
                weight = "500 g",
                imageUrl = "https://images.tokopedia.net/img/cache/250-square/hDjmkQ/2022/1/17/4524771c-4b31-4eb1-9491-0adb581431b1.jpg",
                slashedPrice = "Rp200",
                discountPercentage = "20",
                similarProducts = emptyList(),
                categoryId = "983",
                position = 1
            ),
            RecipeProductUiModel(
                id = "2148241524",
                shopId = "480552",
                name = "kaos testing 113",
                quantity = 100,
                stock = 7,
                minOrder = 1,
                maxOrder = 7,
                priceFmt = "Rp150",
                weight = "500 g",
                imageUrl = "https://images.tokopedia.net/img/cache/250-square/hDjmkQ/2022/1/17/4524771c-4b31-4eb1-9491-0adb581431b1.jpg",
                slashedPrice = "Rp200",
                discountPercentage = "20",
                similarProducts = emptyList(),
                categoryId = "983",
                position = 2
            )
        )

        viewModel.visitableItems
            .verifyValueEquals(expectedProductList)
    }

    @Test
    fun `given mini cart data not set when onViewCreated should update recipe list`() {
        val productList = listOf(
            RecipeProductUiModel(
                id = "2148241523",
                shopId = "480552",
                name = "kaos testing 112",
                quantity = 5,
                stock = 7,
                minOrder = 1,
                maxOrder = 7,
                priceFmt = "Rp150",
                weight = "500 g",
                imageUrl = "https://images.tokopedia.net/img/cache/250-square/hDjmkQ/2022/1/17/4524771c-4b31-4eb1-9491-0adb581431b1.jpg",
                slashedPrice = "Rp200",
                discountPercentage = "20",
                similarProducts = emptyList(),
                categoryId = "983",
                position = 1
            )
        )

        viewModel.onViewCreated(productList)

        val expectedProductList = listOf(
            RecipeProductUiModel(
                id = "2148241523",
                shopId = "480552",
                name = "kaos testing 112",
                quantity = 5,
                stock = 7,
                minOrder = 1,
                maxOrder = 7,
                priceFmt = "Rp150",
                weight = "500 g",
                imageUrl = "https://images.tokopedia.net/img/cache/250-square/hDjmkQ/2022/1/17/4524771c-4b31-4eb1-9491-0adb581431b1.jpg",
                slashedPrice = "Rp200",
                discountPercentage = "20",
                similarProducts = emptyList(),
                categoryId = "983",
                position = 1
            )
        )

        viewModel.visitableItems
            .verifyValueEquals(expectedProductList)
    }

    @Test
    fun `given layout list throws error when set mini cart data should do nothing`() {
        onGetLayoutItemList_returnNull()

        viewModel.setMiniCartData(MiniCartSimplifiedData())

        viewModel.visitableItems
            .verifyValueEquals(null)
    }
}
