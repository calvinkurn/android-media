package com.tokopedia.globalnavwidget

import android.graphics.Rect
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.math.cos
import kotlin.math.roundToInt

internal class GlobalNavWidgetCardItemDecoration(
        private val spacingBetweenCardInPixel: Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (view is CardView) {
            setLeftMargin(position, view, outRect)
            setRightMargin(position, view, outRect, parent)
            setBottomMargin(view, outRect)
        }
    }

    private fun setLeftMargin(position: Int, view: CardView, outRect: Rect) {
        if (isLeftMostItem(position)) {
            outRect.left = outRect.left - view.getHorizontalOffset()
        }
        else {
            outRect.left = (spacingBetweenCardInPixel / 2) - view.getHorizontalOffset()
        }
    }

    private fun isLeftMostItem(position: Int): Boolean {
        return position == 0
    }

    private fun setRightMargin(position: Int, view: CardView, outRect: Rect, parent: RecyclerView) {
        if (isRightMostItem(position, parent)) {
            outRect.right = outRect.right - view.getHorizontalOffset()
        }
        else {
            outRect.right = (spacingBetweenCardInPixel / 2) - view.getHorizontalOffset()
        }
    }

    private fun isRightMostItem(position: Int, parent: RecyclerView): Boolean {
        val itemCount = parent.adapter?.itemCount ?: 0

        return position == itemCount - 1
    }

    private fun setBottomMargin(view: CardView, outRect: Rect) {
        outRect.bottom = outRect.bottom - view.getVerticalOffset()
    }

    private fun CardView.getHorizontalOffset(): Int {
        val maxElevation = this.maxCardElevation
        val radius = this.radius

        return ((maxElevation + (1 - cos(45.0)) * radius).toFloat() * 0.75).roundToInt()
    }

    private fun CardView.getVerticalOffset(): Int {
        val maxElevation = this.maxCardElevation
        val radius = this.radius

        return ((maxElevation * 1.5 + (1 - cos(45.0)) * radius).toFloat() * 0.75).roundToInt()
    }
}