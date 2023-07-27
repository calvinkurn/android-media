package com.tokopedia.search.result.product.seamlessinspirationcard.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class KeywordItemDecoration(private val spacing: Int, private val spanQty: Int) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val absolutePos = parent.getChildAdapterPosition(view)
        val totalSpanCount = spanQty
        val column: Int = absolutePos % totalSpanCount

        val left = column * spacing / totalSpanCount
        val right = spacing - (column + 1) * spacing / totalSpanCount
        val top = spacing
        outRect.left = left
        outRect.right = right
        outRect.top = top
    }
}
