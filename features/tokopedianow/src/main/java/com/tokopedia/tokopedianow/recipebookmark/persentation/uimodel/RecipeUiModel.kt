package com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.recipebookmark.persentation.adapter.RecipeBookmarkTypeFactory

data class RecipeUiModel(
    val id: String,
    val title: String,
    val duration: Int?,
    val portion: Int,
    val tags: List<TagUiModel>?,
    val picture: String,
    val appUrl: String
): Visitable<RecipeBookmarkTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: RecipeBookmarkTypeFactory): Int = typeFactory.type(this)
}