package com.tokopedia.sellerorder.common.presenter

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero

class RecyclerViewItemDivider(
    private val divider: Drawable?,
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
        val itemPosition = parent.getChildAdapterPosition(view)
        val isLastItem: Boolean = itemPosition == parent.adapter?.itemCount.orZero() - 1
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin = if (itemPosition == 0) {
            topMargin
        } else {
            topMargin + divider?.intrinsicHeight.orZero()
        }
        if (!isLastItem || applyMarginAfterLastItem) {
            layoutParams.bottomMargin = bottomMargin
        } else {
            layoutParams.bottomMargin = Int.ZERO
        }
        view.layoutParams = layoutParams
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        divider?.let { divider ->
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
}