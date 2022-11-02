package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeInstructionTypeFactory

abstract class SectionTitleUiModel(
    @StringRes val resId: Int
): Visitable<RecipeInstructionTypeFactory> {

    override fun type(typeFactory: RecipeInstructionTypeFactory): Int {
        return typeFactory.type(this)
    }

    object IngredientSectionTitle: SectionTitleUiModel(R.string.tokopedianow_recipe_ingredient_section_title)
    object InstructionSectionTitle: SectionTitleUiModel(R.string.tokopedianow_recipe_instruction_section_title)
}