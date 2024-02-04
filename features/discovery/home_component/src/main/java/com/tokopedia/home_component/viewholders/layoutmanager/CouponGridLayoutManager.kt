package com.tokopedia.home_component.viewholders.layoutmanager

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CouponGridLayoutManager : RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)

        val itemCount = itemCount
        if (itemCount == 0) {
            return
        }

        var currentRowIndex = 0
        var currentRowHeight = 0
        var maxRowHeight = 0
        val maxCols = 2

        for (i in 0 until itemCount) {
            val view = recycler.getViewForPosition(i)
            addView(view)

            measureChildWithMargins(view, 0, 0)

            val width = getDecoratedMeasuredWidth(view)
            val height = getDecoratedMeasuredHeight(view)

            if (currentRowHeight == 0) {
                // This is the first item in the row, make it match_parent
                layoutDecoratedWithMargins(view, 0, currentRowIndex, width, currentRowIndex + height)
                currentRowHeight = height
            } else {
                // These are items that will be merged into the second row
                val col = (i - 1) % maxCols
                layoutDecoratedWithMargins(
                    view,
                    col * (width / maxCols),
                    currentRowIndex,
                    col * (width / maxCols) + width,
                    currentRowIndex + height
                )

                // Update current row height
                currentRowHeight = currentRowHeight.coerceAtLeast(height)
            }

            if (i % maxCols == maxCols - 1) {
                // Move to the next row
                currentRowIndex += currentRowHeight
                maxRowHeight = maxRowHeight.coerceAtLeast(currentRowHeight)
                currentRowHeight = 0
            }
        }

        // Adjust the height of the RecyclerView
        val totalHeight = currentRowIndex + maxRowHeight
        setMeasuredDimension(width, totalHeight)
    }
}
