package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.BuyAllProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel

interface RecipeIngredientTypeFactory {

    fun type(uiModel: BuyAllProductUiModel): Int
    fun type(uiModel: RecipeProductUiModel): Int
}