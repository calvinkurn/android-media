package com.tokopedia.shop.common.widget

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class RecyclerViewPadding(private val padding: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        parent.adapter?.also {
            if (position == 0) outRect.set(padding, 0, padding, 0)
        }
    }

}