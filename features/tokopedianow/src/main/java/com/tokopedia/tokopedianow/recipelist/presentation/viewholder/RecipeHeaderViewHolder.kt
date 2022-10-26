package com.tokopedia.tokopedianow.recipelist.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeHeaderUiModel

class RecipeHeaderViewHolder(itemView: View): AbstractViewHolder<RecipeHeaderUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_recipe_list_header
    }

    override fun bind(element: RecipeHeaderUiModel?) {
    }
}