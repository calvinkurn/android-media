package com.tokopedia.gallery.customview

import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

class GalleryItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State?) {
        val position = parent.getChildAdapterPosition(view)
        val totalItem = parent.adapter.itemCount

        val totalSpanCount = getTotalSpanCount(parent)

        outRect.top = if (isTopProductItem(position, totalSpanCount)) spacing * 2 else spacing / 2
        outRect.left = spacing / 2
        outRect.right = spacing / 2
        outRect.bottom = if (isBottomProductItem(position, totalSpanCount, totalItem)) spacing * 2 else spacing / 2
    }

    private fun isTopProductItem(position: Int, totalSpanCount: Int): Boolean {
        return position < totalSpanCount
    }

    private fun isBottomProductItem(position: Int, totalSpanCount: Int, totalItem: Int): Boolean {
        return position + totalSpanCount - position % totalSpanCount > totalItem - 1
    }

    private fun isFirstInRow(relativePos: Int, spanCount: Int): Boolean {
        return relativePos % spanCount == 0
    }

    private fun isLastInRow(relativePos: Int, spanCount: Int): Boolean {
        return isFirstInRow(relativePos + 1, spanCount)
    }

    private fun getTotalSpanCount(parent: RecyclerView): Int {
        return (parent.layoutManager as GridLayoutManager).spanCount
    }
}
