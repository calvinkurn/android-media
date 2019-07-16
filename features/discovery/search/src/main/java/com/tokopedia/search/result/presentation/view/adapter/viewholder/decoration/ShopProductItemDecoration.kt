package com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ShopProductItemDecoration(
        val spacing: Int,
        val itemCount: Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = if (parent.getChildAdapterPosition(view) == 0) 0 else spacing / 2
        outRect.top = 0
        outRect.right = if (parent.getChildAdapterPosition(view) == itemCount - 1) 0 else spacing / 2
        outRect.bottom = 0
    }
}