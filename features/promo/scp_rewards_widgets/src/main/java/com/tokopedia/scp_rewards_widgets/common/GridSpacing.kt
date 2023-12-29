package com.tokopedia.scp_rewards_widgets.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class GridSpacing(
    private val horizontalSpacing: Int,
    private val verticalSpacing: Int,
    private val firstRowTopSpacing: Int = 0
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        val totalSpanCount = getTotalSpanCount(parent)
        val spanSize = getItemSpanSize(parent, position)

        outRect.top = if (isInTheFirstRow(position, totalSpanCount)) firstRowTopSpacing else verticalSpacing.toPx()
        outRect.left = if (isFirstInRow(position, totalSpanCount, spanSize)) 0 else horizontalSpacing.toPx() / 2
        outRect.right = if (isLastInRow(position, totalSpanCount, spanSize)) 0 else horizontalSpacing.toPx() / 2
        outRect.bottom = 0
    }

    private fun isInTheFirstRow(position: Int, totalSpanCount: Int): Boolean =
        position < totalSpanCount

    private fun isFirstInRow(position: Int, totalSpanCount: Int, spanSize: Int): Boolean =
        if (totalSpanCount != spanSize) {
            position % totalSpanCount == 0
        } else {
            true
        }

    private fun isLastInRow(position: Int, totalSpanCount: Int, spanSize: Int): Boolean =
        isFirstInRow(position + 1, totalSpanCount, spanSize)

    private fun getTotalSpanCount(parent: RecyclerView): Int =
        (parent.layoutManager as? GridLayoutManager)?.spanCount ?: 1

    private fun getItemSpanSize(parent: RecyclerView, position: Int): Int =
        (parent.layoutManager as? GridLayoutManager)?.spanSizeLookup?.getSpanSize(position) ?: 1
}
