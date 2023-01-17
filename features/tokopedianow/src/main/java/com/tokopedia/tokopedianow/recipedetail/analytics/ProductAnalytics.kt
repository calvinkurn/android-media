package com.tokopedia.tokopedianow.recipedetail.analytics

import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel

interface ProductAnalytics {

    fun trackImpressionProduct(product: RecipeProductUiModel)

    fun trackClickProduct(product: RecipeProductUiModel)

    fun trackClickAddToCart(product: RecipeProductUiModel)

    fun trackImpressionOutOfStockProduct(product: RecipeProductUiModel)

    fun trackClickRemoveProduct()

    fun trackClickDecreaseQuantity()

    fun trackClickIncreaseQuantity()

    fun trackImpressionSimilarProductBtn()

    fun trackClickSimilarProductBtn()
}