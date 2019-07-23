package com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ShopProductItemDecoration(
        private val spacing: Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        outRect.left = getLeftOffset(itemPosition)
        outRect.top = 0
        outRect.right = getRightOffset(itemPosition, itemCount)
        outRect.bottom = 0
    }

    private fun getLeftOffset(itemPosition: Int): Int {
        return if (itemPosition == 0) spacing else spacing / 2
    }

    private fun getRightOffset(itemPosition: Int, itemCount: Int): Int {
        return if (itemPosition == itemCount - 1) spacing else spacing / 2
    }
}