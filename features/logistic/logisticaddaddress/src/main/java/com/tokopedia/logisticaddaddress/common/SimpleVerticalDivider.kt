package com.tokopedia.logisticaddaddress.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.unifycomponents.toPx
import kotlin.math.roundToInt

internal class SimpleVerticalDivider(context: Context, @LayoutRes private val exclude: Int? = null)
    : RecyclerView.ItemDecoration() {

    private val mDivider = ContextCompat.getDrawable(context, R.drawable.item_divider)
    private val mBounds = Rect()

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || mDivider == null) {
            return
        }
        canvas.save()

        val left = 16.toPx()
        val right = parent.width

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            if (parent.getViewTypeOfView(child) != exclude) continue
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val bottom = mBounds.bottom + child.translationY.roundToInt()
            val top = bottom - mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val viewType = parent.getViewTypeOfView(view)
        if (mDivider == null || viewType != exclude) {
            outRect.setEmpty()
        } else {
            outRect.set(0, 0, 0, mDivider.intrinsicHeight)
        }
    }

    private fun RecyclerView.getViewTypeOfView(view: View): Int =
            adapter?.getItemViewType(getChildAdapterPosition(view)) ?: 0

}