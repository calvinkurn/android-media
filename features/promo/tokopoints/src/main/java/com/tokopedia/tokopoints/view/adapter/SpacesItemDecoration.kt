package com.tokopedia.tokopoints.view.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(
    private val space:Int,
    private val leftSpace:Int,
    private val rightSpace:Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        // Add top margin only for the first item to avoid double space between items
        val position = parent.getChildAdapterPosition(view)

        outRect.left = leftSpace
        outRect.right = rightSpace

        if (position == 0) outRect.top = space

        // exclude extra space for last child
        if (parent.adapter!=null && position != parent.adapter!!.itemCount - 1) {
            outRect.bottom = space
        }

    }
}
