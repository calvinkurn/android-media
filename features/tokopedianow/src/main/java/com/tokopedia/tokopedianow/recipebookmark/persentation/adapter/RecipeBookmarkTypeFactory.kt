package com.tokopedia.tokopedianow.recipebookmark.persentation.adapter

import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeProgressBarUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeShimmeringUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel

interface RecipeBookmarkTypeFactory {
    fun type(recipeUiModel: RecipeUiModel): Int
    fun type(recipeProgressBarUiModel: RecipeProgressBarUiModel): Int
    fun type(recipeShimmeringUiModel: RecipeShimmeringUiModel): Int
}