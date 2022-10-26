package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.SectionTitleUiModel

interface RecipeInstructionTypeFactory {

    fun type(uiModel: SectionTitleUiModel): Int
    fun type(uiModel: IngredientUiModel): Int
    fun type(uiModel: InstructionUiModel): Int
}