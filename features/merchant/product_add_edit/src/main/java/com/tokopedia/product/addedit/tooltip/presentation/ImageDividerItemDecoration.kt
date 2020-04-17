package com.tokopedia.product.addedit.tooltip.presentation

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.dpToPx

class ImageDividerItemDecoration(
        private val drawable: Drawable,
        private val drawOnLastItem: Boolean = true,
        private val paddingLeft: Int = 0,
        private val paddingRight: Int = 0,
        private val paddingBottom: Int = 0,
        private val paddingTop: Int = 0) : RecyclerView.ItemDecoration() {

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val displayMetrics = Resources.getSystem().displayMetrics
        val dividerLeft = paddingLeft.dpToPx(displayMetrics) + parent.paddingLeft
        val dividerRight = parent.width - paddingRight.dpToPx(displayMetrics) - parent.paddingRight

        val childCount = parent.childCount
        val lastChildIndex = if (drawOnLastItem) childCount - 1 else childCount - 2
        for (i in 0..lastChildIndex) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + drawable.intrinsicHeight

            drawable.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            drawable.draw(canvas)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        with(parent) {
            val childPosition = getChildAdapterPosition(view)
            adapter?.run {
                if (childPosition in 0 until itemCount) {
                    outRect.top = this@ImageDividerItemDecoration.paddingTop
                    outRect.bottom = this@ImageDividerItemDecoration.paddingBottom
                }
            }
        }
    }
}