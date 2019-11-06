package com.tokopedia.tokopoints.view.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class AddPointsItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)

        when {
            itemPosition % 4 == 0 -> {
                outRect.left = 22
            }
            (itemPosition - 3) % 4 == 0 -> {
                outRect.right = 22
            }
            else -> {
                outRect.left = 28
                outRect.right = 28
            }
        }
    }

}