package com.tokopedia.tokopoints.view.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ColumnItemDecoration(private val space: Int, private val leftSpace: Int, private val rightSpace: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            outRect.right = rightSpace
        }

        else{
            outRect.left = leftSpace
        }
    }

}