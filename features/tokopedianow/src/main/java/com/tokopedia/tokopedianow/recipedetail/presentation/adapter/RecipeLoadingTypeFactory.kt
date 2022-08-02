package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.LoadingUiModel

interface RecipeLoadingTypeFactory {

    fun type(uiModel: LoadingUiModel): Int
}