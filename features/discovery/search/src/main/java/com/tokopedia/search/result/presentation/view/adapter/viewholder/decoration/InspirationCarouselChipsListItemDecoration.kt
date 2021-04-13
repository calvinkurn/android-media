package com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class InspirationCarouselChipsListItemDecoration(
        private val start: Int,
        private val end: Int,
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (isFirstItem(view, parent))
            outRect.left = start
        else
            outRect.left = start / 8

        if (isLastItem(parent, view))
            outRect.right = end
        else
            outRect.right = end / 8
    }

    private fun isFirstItem(view: View, parent: RecyclerView) = parent.getChildAdapterPosition(view) == 0

    private fun isLastItem(parent: RecyclerView, view: View) =
            parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount ?: 0) - 1
}