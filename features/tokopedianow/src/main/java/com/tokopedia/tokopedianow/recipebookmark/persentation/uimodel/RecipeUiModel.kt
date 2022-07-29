package com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipebookmark.persentation.adapter.RecipeBookmarkTypeFactory

data class RecipeUiModel(
    val id: String,
    val title: String,
    val duration: Int?,
    val portion: Int,
    val tags: List<String>?,
    val isOtherTag: Boolean,
    val picture: String
): Visitable<RecipeBookmarkTypeFactory> {
    override fun type(typeFactory: RecipeBookmarkTypeFactory): Int = typeFactory.type(this)
}