package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.MediaSliderUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeDetailLoadingUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeTabUiModel

interface RecipeDetailTypeFactory {

    fun type(uiModel: MediaSliderUiModel): Int
    fun type(uiModel: RecipeInfoUiModel): Int
    fun type(uiModel: RecipeTabUiModel): Int
    fun type(uiModel: RecipeDetailLoadingUiModel): Int
}