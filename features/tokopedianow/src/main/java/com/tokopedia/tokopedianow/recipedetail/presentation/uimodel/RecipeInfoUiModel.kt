package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.TagUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeDetailTypeFactory

data class RecipeInfoUiModel(
    val id: String,
    val title: String,
    val portion: Int,
    val duration: Int,
    val tags: List<TagUiModel>,
    val thumbnail: String,
    val imageUrls: List<String>,
    val shareUrl: String
): Visitable<RecipeDetailTypeFactory> {

    override fun type(typeFactory: RecipeDetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}
