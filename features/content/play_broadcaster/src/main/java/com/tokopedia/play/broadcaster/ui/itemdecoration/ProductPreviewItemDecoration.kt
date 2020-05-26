package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.ui.viewholder.ProductPreviewViewHolder

/**
 * Created by jegul on 26/05/20
 */
class ProductPreviewItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val viewHolder = parent.findViewHolderForAdapterPosition(position)
        val gridLayoutManager = parent.layoutManager as GridLayoutManager

        if (isSameSpanCountWithPrevious(position, gridLayoutManager)
                && isSameSpanGroupIndexWithPrevious(position, gridLayoutManager)) {
            if (viewHolder is ProductPreviewViewHolder) outRect.top = viewHolder.spacing
        }

        if (!isFirstSpanGroupIndex(position, gridLayoutManager) && !isSameSpanGroupIndexWithPrevious(position, gridLayoutManager)) {
            if (viewHolder is ProductPreviewViewHolder) outRect.left = viewHolder.spacing
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
}