package com.tokopedia.tokomart.categorylist.presentation.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomart.categorylist.presentation.viewholder.CategoryListChildViewHolder

class TokoMartCategoryListDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = parent.getChildAdapterPosition(view)
        val viewHolder = parent.findViewHolderForLayoutPosition(itemPosition)
        val lastIndex = state.itemCount - 1
        val isFirstItem = itemPosition == 0
        val isLastItem = itemPosition == lastIndex

        if ((isFirstItem || isLastItem) && viewHolder is CategoryListChildViewHolder) {
            viewHolder.hideDivider()
        }
    }
}