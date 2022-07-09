package com.tokopedia.tokopedianow.recipebookmark.persentation.adapter

import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeProgressBarUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel

interface RecipeBookmarkTypeFactory {
    fun type(uiModel: RecipeUiModel): Int
    fun type(recipeProgressBarUiModel: RecipeProgressBarUiModel): Int
}