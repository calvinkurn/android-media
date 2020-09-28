package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.ui.viewholder.SpacingProvider

/**
 * Created by jegul on 26/05/20
 */
class ProductPreviewItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val viewHolder = parent.findViewHolderForAdapterPosition(position)
        val gridLayoutManager = parent.layoutManager as GridLayoutManager
        val spanSizeLookup = gridLayoutManager.spanSizeLookup
        val spanCount = gridLayoutManager.spanCount

        val isSameSpanGroupIndexWithPrev = isSameSpanGroupIndexWithPrevious(position, gridLayoutManager)

        if (!isSameSpanGroupIndexWithPrev && spanSizeLookup.getSpanSize(position) != spanCount) {
            if (viewHolder is SpacingProvider) outRect.bottom = viewHolder.spacing / 2
        }

        if (isSameSpanCountWithPrevious(position, gridLayoutManager) && isSameSpanGroupIndexWithPrev) {
            if (viewHolder is SpacingProvider) outRect.top = viewHolder.spacing / 2
        }

        if (!isFirstSpanGroupIndex(position, gridLayoutManager)) {
            if (viewHolder is SpacingProvider) outRect.left = viewHolder.spacing / 2
        }

        if (!isLastSpanGroupIndex(position, gridLayoutManager)) {
            if (viewHolder is SpacingProvider) outRect.right = viewHolder.spacing / 2
        }
    }

    private fun isSameSpanCountWithPrevious(position: Int, gridLayoutManager: GridLayoutManager): Boolean {
        if (position == 0) return false
        val spanSizeLookup = gridLayoutManager.spanSizeLookup

        return (spanSizeLookup.getSpanSize(position) == spanSizeLookup.getSpanSize(position - 1))
    }

    private fun isSameSpanGroupIndexWithPrevious(position: Int, gridLayoutManager: GridLayoutManager): Boolean {
        if (position == 0) return false
        val spanSizeLookup = gridLayoutManager.spanSizeLookup
        val spanCount = gridLayoutManager.spanCount

        return (spanSizeLookup.getSpanGroupIndex(position, spanCount) == spanSizeLookup.getSpanGroupIndex(position - 1, spanCount))
    }

    private fun isFirstSpanGroupIndex(position: Int, gridLayoutManager: GridLayoutManager): Boolean {
        val spanSizeLookup = gridLayoutManager.spanSizeLookup
        val spanCount = gridLayoutManager.spanCount
        return spanSizeLookup.getSpanGroupIndex(position, spanCount) == 0
    }

    private fun isLastSpanGroupIndex(position: Int, gridLayoutManager: GridLayoutManager): Boolean {
        val itemCount = gridLayoutManager.itemCount
        if (position == itemCount - 1) return true

        val spanSizeLookup = gridLayoutManager.spanSizeLookup
        val spanCount = gridLayoutManager.spanCount
        return spanSizeLookup.getSpanGroupIndex(position, spanCount) == spanSizeLookup.getSpanGroupIndex(itemCount - 1, spanCount)
    }
}