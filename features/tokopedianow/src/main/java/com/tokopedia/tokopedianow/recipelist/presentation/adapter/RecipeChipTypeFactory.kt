package com.tokopedia.tokopedianow.recipelist.presentation.adapter

import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipUiModel

interface RecipeChipTypeFactory {

    fun type(uiModel: RecipeChipUiModel): Int
}