package com.tokopedia.tokopedianow.recipebookmark.presentation.ui.model

import androidx.compose.runtime.Immutable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.oldrecipebookmark.presentation.adapter.RecipeBookmarkTypeFactory
import com.tokopedia.tokopedianow.recipecommon.ui.model.TagUiModel

@Immutable
data class RecipeUiModel(
    val id: String,
    val title: String,
    val duration: Int?,
    val portion: Int,
    val tags: List<TagUiModel>?,
    val picture: String,
    val appUrl: String
) : Visitable<RecipeBookmarkTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: RecipeBookmarkTypeFactory): Int = typeFactory.type(this)

    fun getUniqueId(): String {
        return this.id + this.hashCode()
    }
}
