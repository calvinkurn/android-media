package com.tokopedia.tokopedianow.recipelist.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeChipViewHolder

class RecipeChipAdapterTypeFactory : BaseAdapterTypeFactory(), RecipeChipTypeFactory {

    override fun type(uiModel: RecipeChipUiModel): Int = RecipeChipViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RecipeChipViewHolder.LAYOUT -> RecipeChipViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}