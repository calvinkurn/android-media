package com.tokopedia.tokopedianow.categoryfilter.presentation.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CategoryChipFilterDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val viewHolder = parent.getChildViewHolder(view)
        val itemView = viewHolder.itemView
        val spacing = itemView.context.resources
            .getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)

        outRect.right = spacing
        outRect.bottom = spacing
    }
}