package com.tokopedia.globalnavwidget

import android.graphics.Rect
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlin.math.cos
import kotlin.math.roundToInt

internal class GlobalNavWidgetCardItemDecoration(
        private val spacingCardTop: Int,
        private val spacingCardToContainer: Int,
        private val spacingBetweenCardInPixel: Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (view is CardView) {
            setLeftMargin(position, view, outRect)
            setTopMargin(view, outRect)
            setRightMargin(position, view, outRect, parent)
            setBottomMargin(view, outRect)
        }
    }

    private fun setLeftMargin(position: Int, view: CardView, outRect: Rect) {
        if (isLeftMostItem(position)) {
            outRect.left = spacingCardToContainer - view.getHorizontalOffset()
        }
        else {
            outRect.left = (spacingBetweenCardInPixel / 2) - view.getHorizontalOffset()
        }
    }

    private fun isLeftMostItem(position: Int): Boolean {
        return position == 0
    }

    private fun setTopMargin(view: CardView, outRect: Rect) {
        outRect.top = spacingCardTop - view.getVerticalOffset()
    }

    private fun setRightMargin(position: Int, view: CardView, outRect: Rect, parent: RecyclerView) {
        if (isRightMostItem(position, parent)) {
            outRect.right = spacingBetweenCardInPixel - view.getHorizontalOffset()
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
        outRect.bottom = spacingCardToContainer - view.getVerticalOffset()
    }

    private fun CardView.getHorizontalOffset(): Int {
        val maxElevation = this.maxCardElevation
        val radius = this.radius

        return ((maxElevation + (1 - cos(45.0)) * radius).toFloat() / 2).roundToInt()
    }

    private fun CardView.getVerticalOffset(): Int {
        val maxElevation = this.maxCardElevation
        val radius = this.radius

        return ((maxElevation * 1.5 + (1 - cos(45.0)) * radius).toFloat() / 2).roundToInt()
    }
}