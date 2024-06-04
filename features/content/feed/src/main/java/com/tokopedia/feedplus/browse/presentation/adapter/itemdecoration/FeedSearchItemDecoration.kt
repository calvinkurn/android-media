package com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class FeedSearchItemDecoration(
    private val spanCount: Int
): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutPosition = parent.getChildLayoutPosition(view)
        if (layoutPosition == RecyclerView.NO_POSITION || layoutPosition >= state.itemCount) return

        if (layoutPosition < spanCount) {
            view.setGapOnFirstRow()
        }
    }

    private fun View.setGapOnFirstRow() {
        setPadding(paddingLeft, 16.toPx(), paddingRight, paddingBottom)
    }
}
