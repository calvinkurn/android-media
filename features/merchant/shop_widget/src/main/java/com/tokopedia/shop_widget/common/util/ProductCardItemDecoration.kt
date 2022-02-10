package com.tokopedia.shop_widget.common.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ProductCardItemDecoration(private val marginLeft: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        // only for the first one
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = marginLeft
        }
    }
}