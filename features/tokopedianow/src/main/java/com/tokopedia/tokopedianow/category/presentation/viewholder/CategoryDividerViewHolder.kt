package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryDividerUiModel

class CategoryDividerViewHolder(
    itemView: View
) : AbstractViewHolder<CategoryDividerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_divider
    }

    override fun bind(element: CategoryDividerUiModel) {
        (itemView.layoutParams as? ViewGroup.MarginLayoutParams)?.let { layoutParams ->
            layoutParams.topMargin = itemView.context.dpToPx(element.topMargin).toInt()
            layoutParams.bottomMargin = itemView.context.dpToPx(element.bottomMargin).toInt()
            itemView.layoutParams = layoutParams
        }
    }
}
