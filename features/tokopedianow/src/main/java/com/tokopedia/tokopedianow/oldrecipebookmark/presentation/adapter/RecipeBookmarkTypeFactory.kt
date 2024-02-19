package com.tokopedia.tokopedianow.oldrecipebookmark.presentation.adapter

import com.tokopedia.tokopedianow.recipebookmark.presentation.uimodel.RecipeProgressBarUiModel
import com.tokopedia.tokopedianow.recipebookmark.presentation.uimodel.RecipeShimmeringUiModel
import com.tokopedia.tokopedianow.recipebookmark.presentation.uimodel.RecipeUiModel

interface RecipeBookmarkTypeFactory {
    fun type(recipeUiModel: RecipeUiModel): Int
    fun type(recipeProgressBarUiModel: RecipeProgressBarUiModel): Int
    fun type(recipeShimmeringUiModel: RecipeShimmeringUiModel): Int
}
