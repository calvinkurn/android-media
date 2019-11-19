package com.tokopedia.similarsearch

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.productcard.v2.ProductCardView
import kotlin.math.cos
import kotlin.math.roundToInt

internal class SimilarSearchItemDecoration(
        private val margin: Int
): RecyclerView.ItemDecoration() {

    private val allowedViewTypes = listOf(
            0
    )

    private var horizontalCardViewOffset: Int = 0
    private var verticalCardViewOffset: Int = 0

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        val position = parent.getChildAdapterPosition(view)

        if (isProductItem(parent, position)) {
            val relativePosition = getProductItemRelativePosition(parent, view)
            val totalSpanCount = getTotalSpanCount(parent)

            horizontalCardViewOffset = getHorizontalCardViewOffset(view)
            verticalCardViewOffset = getVerticalCardViewOffset(view)

            outRect.left = getLeftOffset(relativePosition, totalSpanCount)
            outRect.top = getTopOffset(parent, position, relativePosition, totalSpanCount)
            outRect.right = getRightOffset(relativePosition, totalSpanCount)
            outRect.bottom = getBottomOffset()
        }
    }

    private fun isProductItem(parent: RecyclerView, viewPosition: Int): Boolean {
        val viewType = getRecyclerViewViewType(parent, viewPosition)
        return viewType != -1 && allowedViewTypes.contains(viewType)
    }

    private fun getRecyclerViewViewType(parent: RecyclerView, viewPosition: Int): Int {
        return parent.adapter?.let { getRecyclerViewViewTypeIfAdapterNotNull(viewPosition, it) } ?: -1
    }

    private fun getRecyclerViewViewTypeIfAdapterNotNull(viewPosition: Int, adapter: Adapter<ViewHolder>): Int {
        return if (viewPosition < 0 || viewPosition > adapter.itemCount - 1) {
            -1
        } else {
            adapter.getItemViewType(viewPosition)
        }
    }

    private fun getProductItemRelativePosition(parent: RecyclerView, view: View): Int {
        return if (view.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            getProductItemRelativePositionStaggeredGrid(view)
        }
        else {
            getProductItemRelativePositionNotStaggeredGrid(parent, view)
        }
    }

    private fun getProductItemRelativePositionStaggeredGrid(view: View): Int {
        val staggeredGridLayoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        return staggeredGridLayoutParams.spanIndex
    }

    private fun getProductItemRelativePositionNotStaggeredGrid(parent: RecyclerView, view: View): Int {
        val absolutePos = parent.getChildAdapterPosition(view)
        var firstProductItemPos = absolutePos
        while (isProductItem(parent, firstProductItemPos - 1)) firstProductItemPos--
        return absolutePos - firstProductItemPos
    }

    private fun getTotalSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return if(layoutManager is StaggeredGridLayoutManager) {
            layoutManager.spanCount
        }
        else {
            1
        }
    }

    private fun getHorizontalCardViewOffset(view: View): Int {
        if (view is ProductCardView) {
            val maxElevation = view.getCardViewMaxElevation()
            val radius = view.getCardViewRadius()

            return (maxElevation + (1 - cos(45.0)) * radius).toFloat().roundToInt() / 2
        }

        return 0
    }

    private fun getVerticalCardViewOffset(view: View): Int {
        if (view is ProductCardView) {
            val maxElevation = view.getCardViewMaxElevation()
            val radius = view.getCardViewRadius()

            return (maxElevation * 1.5 + (1 - cos(45.0)) * radius).toFloat().roundToInt() / 2
        }

        return 0
    }

    private fun getLeftOffset(relativePos: Int, totalSpanCount: Int): Int {
        return if (isFirstInRow(relativePos, totalSpanCount)) getLeftOffsetFirstInRow() else getLeftOffsetNotFirstInRow()
    }

    private fun isFirstInRow(relativePos: Int, spanCount: Int): Boolean {
        return relativePos % spanCount == 0
    }

    private fun getLeftOffsetFirstInRow(): Int {
        return margin - horizontalCardViewOffset
    }

    private fun getLeftOffsetNotFirstInRow(): Int {
        return (margin / 4) - horizontalCardViewOffset
    }

    private fun getTopOffset(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Int {
        return if (isTopProductItem(parent, absolutePos, relativePos, totalSpanCount)) getTopOffsetTopItem() else getTopOffsetNotTopItem()
    }

    private fun isTopProductItem(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Boolean {
        return !isProductItem(parent, absolutePos - relativePos % totalSpanCount - 1)
    }

    private fun getTopOffsetTopItem(): Int {
        return margin - verticalCardViewOffset
    }

    private fun getTopOffsetNotTopItem(): Int {
        return margin / 4 - verticalCardViewOffset
    }

    private fun getRightOffset(relativePos: Int, totalSpanCount: Int): Int {
        return if (isLastInRow(relativePos, totalSpanCount)) getRightOffsetLastInRow() else getRightOffsetNotLastInRow()
    }

    private fun isLastInRow(relativePos: Int, spanCount: Int): Boolean {
        return relativePos % spanCount == spanCount - 1
    }

    private fun getRightOffsetLastInRow(): Int {
        return margin - horizontalCardViewOffset
    }

    private fun getRightOffsetNotLastInRow(): Int {
        return (margin / 4) - horizontalCardViewOffset
    }

    private fun getBottomOffset(): Int {
        return (margin / 4) - verticalCardViewOffset
    }
}