package com.tokopedia.autocompletecomponent.universal.presentation.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class UniversalSearchItemDecoration: RecyclerView.ItemDecoration() {
    companion object {
        private const val DIVIDER_HEIGHT_DP = 8
        private const val DIVIDER_MARGIN_TOP_DP = 16
        private const val DIVIDER_MARGIN_BOTTOM_DP = 12
        private const val FIRST_ITEM_MARGIN_TOP_DP = 12
    }

    private var mDivider: Drawable? = null

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        outRect.set(
            0,
            if (position == 0) FIRST_ITEM_MARGIN_TOP_DP.toPx() else 0,
            0,
            (DIVIDER_HEIGHT_DP + DIVIDER_MARGIN_BOTTOM_DP + DIVIDER_MARGIN_TOP_DP).toPx())
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || mDivider == null) {
            return
        }

        val childCount = parent.childCount
        for (x in 0 until childCount) {
            if (x == childCount - 1) {
                continue
            }

            val child = parent.getChildAt(x)
            val left = 0
            val top = child.bottom + DIVIDER_MARGIN_TOP_DP.toPx()
            val right = parent.width
            val bottom = top + DIVIDER_HEIGHT_DP.toPx()

            mDivider?.setBounds(
                left,
                top,
                right,
                bottom
            )
            mDivider?.draw(canvas)
        }

    }

    fun setDrawable(drawable: Drawable) {
        mDivider = drawable
    }
}