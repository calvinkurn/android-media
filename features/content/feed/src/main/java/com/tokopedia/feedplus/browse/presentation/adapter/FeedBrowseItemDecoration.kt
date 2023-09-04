package com.tokopedia.feedplus.browse.presentation.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by meyta.taliti on 22/08/23.
 */
class FeedBrowseItemDecoration(
    private val context: Context,
    @DimenRes spacingHorizontal: Int,
    @DimenRes spacingTop: Int,
) : RecyclerView.ItemDecoration() {

    private val spacingStart = getDimensionInPixel(com.tokopedia.feedplus.R.dimen.feed_space_16)
    private val spacingEnd = getDimensionInPixel(com.tokopedia.feedplus.R.dimen.feed_space_16)
    private val spacingHorizontalPx = getDimensionInPixel(spacingHorizontal)
    private val spacingTopPx = getDimensionInPixel(spacingTop)

    private fun getDimensionInPixel(@DimenRes dimen: Int): Int {
        return context.resources.getDimensionPixelOffset(dimen)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = if (isPositionStart(parent, view)) spacingStart else spacingHorizontalPx

        if (isPositionEnd(parent, view, state)) {
            outRect.right = spacingEnd
        }

        outRect.top = spacingTopPx
    }

    private fun isPositionStart(parent: RecyclerView, view: View): Boolean {
        return parent.getChildAdapterPosition(view) == 0
    }

    private fun isPositionEnd(parent: RecyclerView, view: View, state: RecyclerView.State): Boolean {
        val lastPosition = state.itemCount - 1
        return parent.getChildAdapterPosition(view) == lastPosition
    }
}
