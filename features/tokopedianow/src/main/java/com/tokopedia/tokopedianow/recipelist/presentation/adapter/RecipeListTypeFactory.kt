package com.tokopedia.tokopedianow.recipelist.presentation.adapter

import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeCountUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeHeaderUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeUiModel

interface RecipeListTypeFactory {

    fun type(uiModel: RecipeHeaderUiModel): Int
    fun type(uiModel: RecipeCountUiModel): Int
    fun type(uiModel: RecipeUiModel): Int
    fun type(uiModel: RecipeFilterUiModel): Int
}