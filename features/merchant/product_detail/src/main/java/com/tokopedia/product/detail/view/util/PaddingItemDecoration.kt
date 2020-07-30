package com.tokopedia.product.detail.view.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PaddingItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (parent.adapter?.itemCount == 3 && position != 0) {
            parent.adapter?.also {
                outRect.set(0, 0, 0, 8)
            }
        } else {
            parent.adapter?.also {
                outRect.set(0, 0, 10, 0)
            }
        }
    }

}
