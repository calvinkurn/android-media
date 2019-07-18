package com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ShopListItemDecoration(private val left: Int,
                             private val right: Int,
                             private val bottom: Int,
                             private val top: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = left
        outRect.right = right
        outRect.bottom = bottom / 2
        outRect.top = if (isTopProductItem(parent, view)) top else top / 2
    }

    private fun isTopProductItem(parent: RecyclerView, view: View): Boolean {
        return parent.getChildAdapterPosition(view) == 0
    }
}