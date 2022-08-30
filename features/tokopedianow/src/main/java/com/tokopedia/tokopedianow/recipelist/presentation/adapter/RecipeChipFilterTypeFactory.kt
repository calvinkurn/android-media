package com.tokopedia.tokopedianow.recipelist.presentation.adapter

import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipFilterUiModel

interface RecipeChipFilterTypeFactory {

    fun type(uiModel: RecipeChipFilterUiModel): Int
}