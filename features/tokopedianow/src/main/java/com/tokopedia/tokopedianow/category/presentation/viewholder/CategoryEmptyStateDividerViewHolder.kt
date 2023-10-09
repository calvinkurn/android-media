package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.model.CategoryEmptyStateDivider

class CategoryEmptyStateDividerViewHolder(
    itemView: View
) : AbstractViewHolder<CategoryEmptyStateDivider>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_empty_state_divider
    }

    override fun bind(element: CategoryEmptyStateDivider) {
        (itemView.layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            topMargin = itemView.context.dpToPx(element.topMargin).toInt()
            topMargin = itemView.context.dpToPx(element.bottomMargin).toInt()
        }
    }
}
