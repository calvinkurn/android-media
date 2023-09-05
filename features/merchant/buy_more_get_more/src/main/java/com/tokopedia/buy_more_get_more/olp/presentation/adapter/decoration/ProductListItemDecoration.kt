package com.tokopedia.buy_more_get_more.olp.presentation.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.isZero

class ProductListItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val spacing = 32
        val productListItemPosition = 2

        if (position >= productListItemPosition) {
            val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
            if (spanIndex.isZero()) { // 0 is left span, and 1 is right span
                view.setPadding(spacing, 0, 0, 0) //set left padding
            } else {
                view.setPadding(0, 0, spacing, 0) //set right padding
            }
        }
    }
}
