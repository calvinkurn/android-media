package com.tokopedia.product.addedit.common.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalItemDecoration(private val padding: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        parent.adapter?.let {
            outRect.bottom = padding
            if (parent.getChildAdapterPosition(view) != it.itemCount - 1) {
                outRect.right = padding
            }
        }
    }
}
