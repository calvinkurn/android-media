package com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration

import android.graphics.Rect
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.R
import kotlin.math.cos
import kotlin.math.roundToInt

class ProfileItemDecoration(private val left: Int,
                            private val top: Int,
                            private val right: Int,
                            private val bottom: Int) : RecyclerView.ItemDecoration() {

    private val allowedViewTypes = listOf(
            R.layout.search_search_result_profile
    )

    private var horizontalCardViewOffset: Int = 0
    private var verticalCardViewOffset: Int = 0

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {

        val absolutePosition = parent.getChildAdapterPosition(view)

        if(isProfileItem(parent, absolutePosition)) {
            val relativePosition = getProfileItemRelativePosition(parent, view)
            val totalSpanCount = getTotalSpanCount(parent)

            horizontalCardViewOffset = getHorizontalCardViewOffset(view)
            verticalCardViewOffset = getVerticalCardViewOffset(view)

            outRect.left = getLeftOffset()
            outRect.top = getTopOffset(parent, absolutePosition, relativePosition, totalSpanCount)
            outRect.right = getRightOffset()
            outRect.bottom = getBottomOffset()
        }
    }

    private fun isProfileItem(parent: RecyclerView, viewPosition: Int): Boolean {
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

    private fun getProfileItemRelativePosition(parent: RecyclerView, view: View): Int {
        val absolutePos = parent.getChildAdapterPosition(view)
        var firstProfileItemPos = absolutePos
        while (isProfileItem(parent, firstProfileItemPos - 1)) firstProfileItemPos--
        return absolutePos - firstProfileItemPos
    }

    private fun getTotalSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return if(layoutManager is GridLayoutManager) {
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
        return left - horizontalCardViewOffset
    }

    private fun getTopOffset(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Int {
        return getTopOffsetWithCardViewOffset(parent, absolutePos, relativePos, totalSpanCount) - verticalCardViewOffset
    }

    private fun getTopOffsetWithCardViewOffset(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Int {
        return if (isTopProfileItem(parent, absolutePos, relativePos, totalSpanCount)) top else top / 4
    }

    private fun isTopProfileItem(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Boolean {
        return !isProfileItem(parent, absolutePos - relativePos % totalSpanCount - 1)
    }

    private fun getRightOffset(): Int {
        return right - horizontalCardViewOffset
    }

    private fun getBottomOffset(): Int {
        return (bottom / 4) - verticalCardViewOffset
    }
}