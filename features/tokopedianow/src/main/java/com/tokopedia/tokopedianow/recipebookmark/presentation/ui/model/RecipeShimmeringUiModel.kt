package com.tokopedia.tokopedianow.recipebookmark.presentation.ui.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.oldrecipebookmark.presentation.adapter.RecipeBookmarkTypeFactory

data class RecipeShimmeringUiModel(
    val recipeId: String
): Visitable<RecipeBookmarkTypeFactory> {
    override fun type(typeFactory: RecipeBookmarkTypeFactory): Int = typeFactory.type(this)
}
