package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeInstructionTypeFactory

data class IngredientUiModel(
    val name: String,
    val quantity: String,
    val unit: String,
    val isLastItem: Boolean = false
): Visitable<RecipeInstructionTypeFactory> {

    override fun type(typeFactory: RecipeInstructionTypeFactory): Int {
        return typeFactory.type(this)
    }
}