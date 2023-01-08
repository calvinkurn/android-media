package com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeProgressBarUiModel

class RecipeProgressBarViewHolder(
    itemView: View
): AbstractViewHolder<RecipeProgressBarUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_loading_more
    }

    override fun bind(element: RecipeProgressBarUiModel) { /* nothing to do */}
}