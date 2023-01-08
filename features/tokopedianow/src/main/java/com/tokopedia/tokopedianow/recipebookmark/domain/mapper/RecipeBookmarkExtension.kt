package com.tokopedia.tokopedianow.recipebookmark.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeProgressBarUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeShimmeringUiModel

fun MutableList<Visitable<*>>.removeRecipeProgressBar() {
    removeFirst { it is RecipeProgressBarUiModel }
}

fun MutableList<Visitable<*>>.addRecipeProgressBar() {
    add(RecipeProgressBarUiModel())
}

fun MutableList<Visitable<*>>.addRecipeShimmering(position: Int, recipeId: String) {
    add(position, RecipeShimmeringUiModel(recipeId))
}

fun MutableList<Visitable<*>>.removeRecipeShimmering(recipeId: String) {
    removeFirst { it is RecipeShimmeringUiModel && it.recipeId == recipeId }
}

