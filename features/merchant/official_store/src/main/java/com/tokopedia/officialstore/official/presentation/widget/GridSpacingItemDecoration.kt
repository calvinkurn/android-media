package com.tokopedia.officialstore.official.presentation.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(private val spanCount: Int,
                                private val spacing: Int) :
        RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if (position % 2 == 1) {
            outRect.left = spacing - column * spacing / spanCount
        } else {
            outRect.right = (column + 1) * spacing / spanCount
        }

        if (position < spanCount) { // top edge
            outRect.top = spacing
        }
        outRect.bottom = spacing // item bottom
    }
}