package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.BuyAllProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.OutOfCoverageUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel

interface RecipeIngredientTypeFactory {

    fun type(uiModel: BuyAllProductUiModel): Int
    fun type(uiModel: RecipeProductUiModel): Int
    fun type(uiModel: OutOfCoverageUiModel): Int
}