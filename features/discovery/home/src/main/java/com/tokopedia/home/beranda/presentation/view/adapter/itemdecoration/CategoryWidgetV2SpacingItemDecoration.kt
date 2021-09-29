package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R

/**
 * created by Dhaba
 */
class CategoryWidgetV2SpacingItemDecoration(private val spanCount: Int, private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column

        if (position == 0 || position == 1) {
            outRect.left = view.context.resources.getDimensionPixelOffset(R.dimen.dp_16)
        } else {
            outRect.left = spacing/2
        }
        outRect.top = spacing/2
        outRect.bottom = spacing/2
        outRect.right = spacing/2
    }
}