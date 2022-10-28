package com.tokopedia.globalnavwidget.catalog

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.viewallcard.ViewAllCard

class GlobalNavWidgetCatalogItemDecoration(
    private val spacingCardToContainer: Int,
    private val spacingBetweenCardInPixel: Int
): RecyclerView.ItemDecoration() {
    private val viewAllCardShadowOffset = 4.toPx()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        setLeftMargin(position, outRect)
        setTopMargin(outRect)
        setRightMargin(position, outRect, parent)
        setBottomMargin(outRect)

        if (view is ViewAllCard) {
            adjustViewAllCardRect(outRect)
        }
    }

    private fun setLeftMargin(position: Int, outRect: Rect) {
        if (isLeftMostItem(position)) {
            outRect.left = spacingCardToContainer
        }
        else {
            outRect.left = spacingBetweenCardInPixel / 2
        }
    }

    private fun setRightMargin(position: Int, outRect: Rect, parent: RecyclerView) {
        if (isRightMostItem(position, parent)) {
            outRect.right = spacingBetweenCardInPixel
        }
        else {
            outRect.right = spacingBetweenCardInPixel / 2
        }
    }

    private fun setTopMargin(outRect: Rect) {
        outRect.top = spacingCardToContainer
    }

    private fun setBottomMargin(outRect: Rect) {
        outRect.bottom = spacingCardToContainer
    }


    private fun isLeftMostItem(position: Int): Boolean {
        return position == 0
    }

    private fun isRightMostItem(position: Int, parent: RecyclerView): Boolean {
        val itemCount = parent.adapter?.itemCount ?: 0

        return position == itemCount - 1
    }

    private fun adjustViewAllCardRect(outRect: Rect) {
        outRect.top -= viewAllCardShadowOffset
        outRect.bottom -= viewAllCardShadowOffset
        outRect.left -= viewAllCardShadowOffset
        outRect.right += viewAllCardShadowOffset
    }
}