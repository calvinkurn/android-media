package com.tokopedia.tokopoints.view.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class AddPointsItemDecoration(val boundarySpacing: Int, val defaultSpacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)

        when {
           /* itemPosition % 4 == 0 -> {
                outRect.left = boundarySpacing
            }
            itemPosition % 4 == 1 -> {
                outRect.left = boundarySpacing
            }
            itemPosition % 4 == 2 -> {
                outRect.left = boundarySpacing
            }*/
            itemPosition % 4 == 3 -> {
               // outRect.left = boundarySpacing
                outRect.right = boundarySpacing
            }
        }
    }
}
