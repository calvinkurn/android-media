package com.tokopedia.notifcenter.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.notifcenter.R

class ChipFilterItemDivider(context: Context?) : RecyclerView.ItemDecoration() {

    private val dividerMarginRight = 16f.toPx().toInt()
    private val dividerMarginLeft = 8f.toPx().toInt()
    private var divider: Drawable? = null

    interface ChipFilterListener {
        fun getDividerPositions(): List<Int>
    }

    init {
        context?.let {
            divider = ContextCompat.getDrawable(it, R.drawable.divider)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val dividerPositions = getDividerPositions(parent) ?: return
        val childPosition = parent.getChildAdapterPosition(view)

        if (childPosition in dividerPositions) {
            outRect.right = dividerMarginRight + dividerMarginLeft
        } else {
            outRect.right = 0
        }
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerPositions = getDividerPositions(parent) ?: return
        val top = parent.top + parent.paddingTop
        val bottom = parent.bottom  - parent.paddingBottom

        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)
            val childPosition = parent.getChildAdapterPosition(child)
            if (childPosition !in dividerPositions) continue
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin + dividerMarginLeft
            val right = left + 1f.toPx().toInt()

            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }

    private fun getDividerPositions(parent: RecyclerView): List<Int>? {
        val listener = parent.adapter
        if (listener !is ChipFilterListener) return null
        return listener.getDividerPositions()
    }
}