package com.tokopedia.scp_rewards_widgets.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val horizontalSpacing: Int,
    private val verticalSpacing: Int,
    private val includeEdge: Boolean
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: 1
        val column = position % spanCount // item column
        if (includeEdge) {
            outRect.left =
                horizontalSpacing - column * horizontalSpacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right =
                (column + 1) * horizontalSpacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
            if (position < spanCount) { // top edge
                outRect.top = verticalSpacing
            }
            outRect.bottom = verticalSpacing // item bottom
        } else {
            outRect.left = column * horizontalSpacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right =
                horizontalSpacing - (column + 1) * horizontalSpacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = verticalSpacing // item top
            }
        }
    }
}
