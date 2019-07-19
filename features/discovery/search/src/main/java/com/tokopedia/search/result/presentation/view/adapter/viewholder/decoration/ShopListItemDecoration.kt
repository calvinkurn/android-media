package com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration

import android.graphics.Rect
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.math.cos
import kotlin.math.roundToInt

class ShopListItemDecoration(private val left: Int,
                             private val right: Int,
                             private val bottom: Int,
                             private val top: Int) : RecyclerView.ItemDecoration() {

    private var horizontalCardViewOffset: Int = 0
    private var verticalCardViewOffset: Int = 0

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {

        horizontalCardViewOffset = getHorizontalCardViewOffset(view)
        verticalCardViewOffset = getVerticalCardViewOffset(view)

        outRect.left = getLeftOffset()
        outRect.top = getTopOffset(parent, view)
        outRect.right = getRightOffset()
        outRect.bottom = getBottomOffset()
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

    private fun getTopOffsetWithCardViewOffset(parent: RecyclerView, view: View): Int {
        return if (isTopProductItem(parent, view)) top else top / 4
    }

    private fun isTopProductItem(parent: RecyclerView, view: View): Boolean {
        return parent.getChildAdapterPosition(view) == 0
    }

    private fun getRightOffset(): Int {
        return right - horizontalCardViewOffset
    }

    private fun getBottomOffset(): Int {
        return (bottom / 4) - verticalCardViewOffset
    }

    private fun getTopOffset(parent: RecyclerView, view: View): Int {
        return getTopOffsetWithCardViewOffset(parent, view) - verticalCardViewOffset
    }
}