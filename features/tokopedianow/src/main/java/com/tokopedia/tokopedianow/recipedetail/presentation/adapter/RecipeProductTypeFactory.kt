package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel

interface RecipeProductTypeFactory {

    fun type(uiModel: RecipeProductUiModel): Int
}