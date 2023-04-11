package com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeShimmeringUiModel

class RecipeShimmeringViewHolder(
    itemView: View
): AbstractViewHolder<RecipeShimmeringUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_recipe_bookmark_shimmering
    }

    override fun bind(element: RecipeShimmeringUiModel) { /* nothing to do */}
}