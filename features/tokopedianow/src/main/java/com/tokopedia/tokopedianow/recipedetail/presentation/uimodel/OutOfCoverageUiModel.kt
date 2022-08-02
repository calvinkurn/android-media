package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeIngredientTypeFactory

object OutOfCoverageUiModel: Visitable<RecipeIngredientTypeFactory> {
    override fun type(typeFactory: RecipeIngredientTypeFactory): Int {
        return typeFactory.type(this)
    }
}