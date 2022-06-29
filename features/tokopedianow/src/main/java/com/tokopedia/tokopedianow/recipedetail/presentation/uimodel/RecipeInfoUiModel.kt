package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeDetailTypeFactory

data class RecipeInfoUiModel(val title: String): Visitable<RecipeDetailTypeFactory> {

    override fun type(typeFactory: RecipeDetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}