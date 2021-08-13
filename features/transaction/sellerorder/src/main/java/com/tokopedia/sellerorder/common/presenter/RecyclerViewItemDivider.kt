package com.tokopedia.sellerorder.common.presenter

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero

class RecyclerViewItemDivider(
    private val divider: Drawable,
    private val topMargin: Int,
    private val bottomMargin: Int,
    private val applyMarginAfterLastItem: Boolean = false,
    private val drawDividerAfterLastItem: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val isLastItem: Boolean =
            parent.getChildAdapterPosition(view) == parent.adapter?.itemCount.orZero() - 1
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        layoutParams.topMargin = topMargin + divider.intrinsicHeight
        if (!isLastItem || applyMarginAfterLastItem) {
            layoutParams.bottomMargin = bottomMargin
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        val lastItem = if (drawDividerAfterLastItem) childCount else childCount.minus(1)
        for (i in 0 until lastItem) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }
}