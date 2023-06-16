package com.tokopedia.productcard.test.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.productcard.IProductCardView
import kotlin.math.cos
import kotlin.math.roundToInt

internal class ProductCardItemDecoration(private val spacing: Int): RecyclerView.ItemDecoration() {

    private var verticalCardViewOffset = 0
    private var horizontalCardViewOffset = 0

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        verticalCardViewOffset = getVerticalCardViewOffset(view)
        horizontalCardViewOffset = getHorizontalCardViewOffset(view)

        val absolutePos = parent.getChildAdapterPosition(view)
        val relativePos = getProductItemRelativePosition(view)
        val totalSpanCount = getTotalSpanCount(parent)

        outRect.left = getLeftOffset(relativePos, totalSpanCount)
        outRect.top = getTopOffset(absolutePos, totalSpanCount)
        outRect.right = getRightOffset(relativePos, totalSpanCount)
        outRect.bottom = getBottomOffset()
    }

    private fun getVerticalCardViewOffset(view: View): Int {
        if (view is IProductCardView) {
            val maxElevation = view.getCardMaxElevation()
            val radius = view.getCardRadius()
            return (maxElevation * 1.5 + (1 - cos(45.0)) * radius).toFloat().roundToInt() / 2
        }

        return 0
    }

    private fun getHorizontalCardViewOffset(view: View): Int {
        if (view is IProductCardView) {
            val maxElevation = view.getCardMaxElevation()
            val radius = view.getCardRadius()
            return (maxElevation + (1 - cos(45.0)) * radius).toFloat().roundToInt() / 2
        }

        return 0
    }

    private fun getProductItemRelativePosition(view: View): Int {
        return if (view.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            getProductItemRelativePositionStaggeredGrid(view)
        } else 0
    }

    private fun getProductItemRelativePositionStaggeredGrid(view: View): Int {
        val staggeredGridLayoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        return staggeredGridLayoutParams.spanIndex
    }

    private fun getTotalSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return if (layoutManager is StaggeredGridLayoutManager) layoutManager.spanCount else 0
    }

    private fun getLeftOffset(relativePos: Int, totalSpanCount: Int): Int {
        return if (isFirstInRow(relativePos, totalSpanCount)) getLeftOffsetFirstInRow() else getLeftOffsetNotFirstInRow()
    }

    private fun isFirstInRow(relativePos: Int, spanCount: Int): Boolean {
        return relativePos % spanCount == 0
    }

    private fun getLeftOffsetFirstInRow(): Int {
        return spacing - horizontalCardViewOffset
    }

    private fun getLeftOffsetNotFirstInRow(): Int {
        return spacing / 4 - horizontalCardViewOffset
    }

    private fun getTopOffset(absolutePos: Int, totalSpanCount: Int): Int {
        return if (isTopProductItem(absolutePos, totalSpanCount)) getTopOffsetTopItem() else getTopOffsetNotTopItem()
    }

    private fun isTopProductItem(absolutePos: Int, totalSpanCount: Int): Boolean {
        return absolutePos < totalSpanCount
    }

    private fun getTopOffsetTopItem(): Int {
        return spacing - verticalCardViewOffset
    }

    private fun getTopOffsetNotTopItem(): Int {
        return spacing / 4 - verticalCardViewOffset
    }

    private fun getRightOffset(relativePos: Int, totalSpanCount: Int): Int {
        return if (isLastInRow(relativePos, totalSpanCount)) getRightOffsetLastInRow() else getRightOffsetNotLastInRow()
    }

    private fun isLastInRow(relativePos: Int, spanCount: Int): Boolean {
        return relativePos % spanCount == spanCount - 1
    }

    private fun getRightOffsetLastInRow(): Int {
        return spacing - horizontalCardViewOffset
    }

    private fun getRightOffsetNotLastInRow(): Int {
        return spacing / 4 - horizontalCardViewOffset
    }

    private fun getBottomOffset(): Int {
        return spacing / 4 - verticalCardViewOffset
    }
}
