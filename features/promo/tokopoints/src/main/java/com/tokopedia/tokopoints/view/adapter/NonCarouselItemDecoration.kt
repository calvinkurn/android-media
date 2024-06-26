package com.tokopedia.tokopoints.view.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class NonCarouselItemDecoration(
    private val size:Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left += size
        } else if (parent.adapter!=null && parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
            outRect.right = size
        }
    }
}
