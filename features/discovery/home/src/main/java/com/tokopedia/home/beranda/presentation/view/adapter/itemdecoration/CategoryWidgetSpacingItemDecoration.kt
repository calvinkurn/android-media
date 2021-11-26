package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.home_component.util.toDpInt

/**
 * @author by DevAra on 18/05/20.
 */

class CategoryWidgetSpacingItemDecoration(private val spanCount: Int, private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column

        if (position == 0 || position == 1) {
            outRect.left = 14f.toDpInt()
        } else {
            outRect.left = spacing/2
        }
        if(position % 2 == 0) {
            outRect.top = 0
        } else {
            outRect.top = spacing/2
        }

        outRect.bottom = spacing/2
        outRect.right = spacing/2
    }
}