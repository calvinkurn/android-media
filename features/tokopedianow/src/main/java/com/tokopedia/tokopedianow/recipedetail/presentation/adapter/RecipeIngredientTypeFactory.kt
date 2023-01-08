package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.BuyAllProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.OutOfCoverageUiModel

interface RecipeIngredientTypeFactory {

    fun type(uiModel: BuyAllProductUiModel): Int
    fun type(uiModel: OutOfCoverageUiModel): Int
}