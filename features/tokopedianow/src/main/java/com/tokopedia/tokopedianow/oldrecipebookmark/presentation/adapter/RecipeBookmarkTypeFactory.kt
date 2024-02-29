package com.tokopedia.tokopedianow.oldrecipebookmark.presentation.adapter

import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.model.RecipeProgressBarUiModel
import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.model.RecipeShimmeringUiModel
import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.model.RecipeUiModel

interface RecipeBookmarkTypeFactory {
    fun type(recipeUiModel: RecipeUiModel): Int
    fun type(recipeProgressBarUiModel: RecipeProgressBarUiModel): Int
    fun type(recipeShimmeringUiModel: RecipeShimmeringUiModel): Int
}
