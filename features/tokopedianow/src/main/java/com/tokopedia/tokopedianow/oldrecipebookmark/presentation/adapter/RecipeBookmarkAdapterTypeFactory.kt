package com.tokopedia.tokopedianow.oldrecipebookmark.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.model.RecipeProgressBarUiModel
import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.model.RecipeShimmeringUiModel
import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.model.RecipeUiModel
import com.tokopedia.tokopedianow.oldrecipebookmark.presentation.viewholder.RecipeProgressBarViewHolder
import com.tokopedia.tokopedianow.oldrecipebookmark.presentation.viewholder.RecipeShimmeringViewHolder
import com.tokopedia.tokopedianow.oldrecipebookmark.presentation.viewholder.RecipeViewHolder

class RecipeBookmarkAdapterTypeFactory(
    private val listener: RecipeViewHolder.RecipeListener
): BaseAdapterTypeFactory(), RecipeBookmarkTypeFactory {

    override fun type(recipeUiModel: RecipeUiModel): Int = RecipeViewHolder.LAYOUT

    override fun type(recipeProgressBarUiModel: RecipeProgressBarUiModel): Int = RecipeProgressBarViewHolder.LAYOUT

    override fun type(recipeShimmeringUiModel: RecipeShimmeringUiModel): Int = RecipeShimmeringViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            RecipeViewHolder.LAYOUT -> RecipeViewHolder(parent, listener)
            RecipeProgressBarViewHolder.LAYOUT -> RecipeProgressBarViewHolder(parent)
            RecipeShimmeringViewHolder.LAYOUT -> RecipeShimmeringViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
