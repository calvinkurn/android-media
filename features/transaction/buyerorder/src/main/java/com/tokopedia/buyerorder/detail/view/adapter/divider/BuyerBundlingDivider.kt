package com.tokopedia.buyerorder.detail.view.adapter.divider

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero

abstract class BuyerBundlingDivider: RecyclerView.ItemDecoration() {

    abstract val divider: Drawable?
    abstract val padding: Int

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = padding
        val right = parent.width - padding
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + divider?.intrinsicHeight.orZero()
            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }

}