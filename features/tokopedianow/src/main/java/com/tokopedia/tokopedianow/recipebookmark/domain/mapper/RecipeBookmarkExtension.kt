package com.tokopedia.tokopedianow.recipebookmark.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeProgressBarUiModel

fun MutableList<Visitable<*>>.removeRecipeProgressBar() {
    removeFirst { it is RecipeProgressBarUiModel }
}

fun MutableList<Visitable<*>>.addRecipeProgressBar() {
    add(RecipeProgressBarUiModel())
}

