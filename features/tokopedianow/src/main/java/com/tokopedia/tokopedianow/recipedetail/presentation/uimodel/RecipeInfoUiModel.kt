package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeDetailTypeFactory

data class RecipeInfoUiModel(
    val title: String,
    val portion: Int,
    val duration: Int,
    val labels: List<String>,
    val thumbnail: String,
    val imageUrls: List<String>,
    val shareUrl: String
): Visitable<RecipeDetailTypeFactory> {

    override fun type(typeFactory: RecipeDetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}