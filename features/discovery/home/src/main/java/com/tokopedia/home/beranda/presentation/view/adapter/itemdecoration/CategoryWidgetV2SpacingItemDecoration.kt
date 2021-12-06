package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.toDpInt

/**
 * created by Dhaba
 */
class CategoryWidgetV2SpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position

        if (position == 0 || position == 1) {
            outRect.left = 16f.toDpInt()
        } else {
            outRect.left = spacing/2
        }

        if(position % 2 == 1) {
            outRect.top = spacing/2
        } else {
            outRect.top = 0
        }

        outRect.bottom = spacing/2
        outRect.right = spacing/2
    }
}