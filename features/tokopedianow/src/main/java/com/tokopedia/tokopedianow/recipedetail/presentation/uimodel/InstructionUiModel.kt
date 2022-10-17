package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeInstructionTypeFactory

data class InstructionUiModel(
    val htmlText: String
) : Visitable<RecipeInstructionTypeFactory> {

    override fun type(typeFactory: RecipeInstructionTypeFactory): Int {
        return typeFactory.type(this)
    }
}