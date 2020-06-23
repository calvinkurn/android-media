package com.tokopedia.seller.search.common.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class PaddingItemDecorationSearchSeller : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position > 0) {
            parent.adapter.also {
                outRect.left = 8.toPx()
            }
        }
    }
}

class FilterItemDecoration : RecyclerView.ItemDecoration() {

    companion object {
        const val PADDING_ITEM = 8
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = PADDING_ITEM.toPx()
        outRect.top = PADDING_ITEM.toPx() / 2
        outRect.bottom = PADDING_ITEM.toPx() / 2
    }
}