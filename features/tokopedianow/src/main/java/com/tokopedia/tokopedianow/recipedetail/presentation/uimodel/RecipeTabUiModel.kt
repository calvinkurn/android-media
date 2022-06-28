package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeDetailTypeFactory

class RecipeTabUiModel(
    val ingredientTab: List<IngredientTabUiModel>,
    val howToCookTab: List<HowToCookTabUiModel>
): Visitable<RecipeDetailTypeFactory> {

    override fun type(typeFactory: RecipeDetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}