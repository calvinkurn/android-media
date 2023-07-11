package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test

class CategoryWishlistTest: TokoNowCategoryMainViewModelTestFixture() {
    @Test
    fun `when updateWishlistStatus should update the status in layout`()  {
        // mock data
        val parentProductId = "1223"
        val productId = "2025598474"
        val productOrderQuantity = 0
        val productStock = 5
        val productName = "Jeruk"
        val productPrice = "Rp. 2000"
        val productHasBeenWishlist = true
        val showcaseTitle = "Buah- Buahan"
        val showcaseId = "133333"
        val privateFieldNameLayout = "layout"
        val expectedWishlist = !productHasBeenWishlist

        val categoryShowcaseItemUiModel = CategoryShowcaseItemUiModel(
            productCardModel = ProductCardCompactUiModel(
                productId = productId,
                orderQuantity = productOrderQuantity,
                availableStock = productStock,
                name = productName,
                price = productPrice,
                hasBeenWishlist = productHasBeenWishlist
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

        viewModel.updateWishlistStatus(
            productId = productId,
            hasBeenWishlist = expectedWishlist
        )

        viewModel.categoryPage
            .verifyValueEquals(
                mockLayout.map {
                    it.copy(
                        productListUiModels = listOf(
                            categoryShowcaseItemUiModel.copy(
                                productCardModel = categoryShowcaseItemUiModel.productCardModel.copy(
                                    hasBeenWishlist = expectedWishlist
                                )
                            )
                        )
                    )
                }
            )
    }

    @Test
    fun `modify layout while its value is null should make updateWishlistStatus error and do nothing`()  {
        val productId = "2025598474"
        val productHasBeenWishlist = true
        val privateFieldNameLayout = "layout"
        val expectedWishlist = !productHasBeenWishlist

        viewModel.mockPrivateField(
            name = privateFieldNameLayout,
            value = null
        )

        viewModel.updateWishlistStatus(
            productId = productId,
            hasBeenWishlist = expectedWishlist
        )

        viewModel.categoryPage
            .verifyValueEquals(null)
    }
}
