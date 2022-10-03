package com.tokopedia.tokopedianow.recipelist.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.TagUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.RecipeListTypeFactory

data class RecipeUiModel(
    val id: String,
    val title: String,
    val portion: Int,
    val duration: Int,
    val tags: List<TagUiModel>,
    val thumbnail: String,
    val isBookmarked: Boolean
): Visitable<RecipeListTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: RecipeListTypeFactory): Int {
        return typeFactory.type(this)
    }
}