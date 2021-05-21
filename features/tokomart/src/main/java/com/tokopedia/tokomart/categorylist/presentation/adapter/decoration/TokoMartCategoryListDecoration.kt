package com.tokopedia.tokomart.categorylist.presentation.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.tokomart.categorylist.presentation.viewholder.CategoryListItemViewHolder
import kotlinx.android.synthetic.main.item_tokomart_category_list.view.*

class TokoMartCategoryListDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = parent.getChildAdapterPosition(view)
        val viewHolder = parent.findViewHolderForLayoutPosition(itemPosition)
        val lastIndex = state.itemCount - 1
        val isFirstItem = itemPosition == 0
        val isLastItem = itemPosition == lastIndex

        if (isFirstItem && viewHolder is CategoryListItemViewHolder) {
            val marginLeft = view.context.resources
                .getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
            val marginBottom = view.context.resources
                .getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

            viewHolder.itemView.imageCategory
                .setMargin(marginLeft, 0, 0, marginBottom)
        }

        if (isLastItem && viewHolder is CategoryListItemViewHolder) {
            viewHolder.itemView.divider.hide()
        }
    }
}