package com.tokopedia.tokopedianow.recipebookmark.persentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder.RecipeViewHolder

class RecipeBookmarkAdapterTypeFactory(
    private val listener: RecipeViewHolder.RecipeListener
): BaseAdapterTypeFactory(), RecipeBookmarkTypeFactory {

    override fun type(uiModel: RecipeUiModel): Int = RecipeViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            RecipeViewHolder.LAYOUT -> RecipeViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}