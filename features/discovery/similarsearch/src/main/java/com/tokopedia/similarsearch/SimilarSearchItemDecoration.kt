package com.tokopedia.similarsearch

import android.graphics.Rect
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlin.math.cos
import kotlin.math.roundToInt

internal class SimilarSearchItemDecoration(
        private val margin: Int
): RecyclerView.ItemDecoration() {

    private val allowedViewTypes = listOf(
            R.layout.similar_search_product_card_layout
    )

    private var horizontalCardViewOffset: Int = 0
    private var verticalCardViewOffset: Int = 0

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (isProductItem(parent, position)) {
            val relativePosition = getProductItemRelativePosition(parent, view)
            val totalSpanCount = getTotalSpanCount(parent)

            horizontalCardViewOffset = getHorizontalCardViewOffset(view)
            verticalCardViewOffset = getVerticalCardViewOffset(view)

//            outRect.left = getLeftOffset()
//            outRect.top = getTopOffset(parent, absolutePosition, relativePosition, totalSpanCount)
//            outRect.right = getRightOffset()
//            outRect.bottom = getBottomOffset()
        }
    }

    private fun isProductItem(parent: RecyclerView, viewPosition: Int): Boolean {
        val viewType = getRecyclerViewViewType(parent, viewPosition)
        return viewType != -1 && allowedViewTypes.contains(viewType)
    }

    private fun getRecyclerViewViewType(parent: RecyclerView, viewPosition: Int): Int {
        return parent.adapter?.let { getRecyclerViewViewTypeIfAdapterNotNull(viewPosition, it) } ?: -1
    }

    private fun getRecyclerViewViewTypeIfAdapterNotNull(
            viewPosition: Int,
            adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    ): Int {
        return if (viewPosition < 0 || viewPosition > adapter.itemCount - 1) {
            -1
        } else {
            adapter.getItemViewType(viewPosition)
        }
    }

    private fun getProductItemRelativePosition(parent: RecyclerView, view: View): Int {
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
        if (view is CardView) {
            val maxElevation = view.maxCardElevation
            val radius = view.radius

            return (maxElevation + (1 - cos(45.0)) * radius).toFloat().roundToInt() / 2
        }

        return 0
    }

    private fun getVerticalCardViewOffset(view: View): Int {
        if (view is CardView) {
            val maxElevation = view.maxCardElevation
            val radius = view.radius

            return (maxElevation * 1.5 + (1 - cos(45.0)) * radius).toFloat().roundToInt() / 2
        }

        return 0
    }

    private fun getLeftOffset(): Int {
        return margin - horizontalCardViewOffset
    }

    private fun getTopOffset(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Int {
        return getTopOffsetWithCardViewOffset(parent, absolutePos, relativePos, totalSpanCount) - verticalCardViewOffset
    }

    private fun getTopOffsetWithCardViewOffset(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Int {
        return if (isTopProductItem(parent, absolutePos, relativePos, totalSpanCount)) margin else margin / 4
    }

    private fun isTopProductItem(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Boolean {
        return !isProductItem(parent, absolutePos - relativePos % totalSpanCount - 1)
    }

    private fun getRightOffset(): Int {
        return margin - horizontalCardViewOffset
    }

    private fun getBottomOffset(): Int {
        return (margin / 4) - verticalCardViewOffset
    }
}