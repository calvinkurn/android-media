package com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.decoration

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView

/**
 * @author by StevenFredian on 27/02/18.
 */
class SpaceItemDecoration(private var divider: Drawable?) : RecyclerView.ItemDecoration() {

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount - 2) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + divider!!.intrinsicHeight
            divider!!.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            divider!!.draw(canvas)
        }
    }
}