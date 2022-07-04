package com.tokopedia.tokopedianow.recipebookmark.persentation.adapter

import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.MediaSliderUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeTabUiModel

interface RecipeBookmarkTypeFactory {
    fun type(uiModel: RecipeUiModel): Int
}