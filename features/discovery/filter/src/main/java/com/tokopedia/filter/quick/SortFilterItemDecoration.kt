package com.tokopedia.filter.quick

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.unifycomponents.toPx

internal class SortFilterItemDecoration: ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val lastIndex = (parent.adapter?.itemCount ?: 0) - 1
        val leftMargin = if (position > 0) 2.toPx() else 0
        val rightMargin = if (position < lastIndex) 2.toPx() else 0

        outRect.left = leftMargin
        outRect.right = rightMargin
    }
}
