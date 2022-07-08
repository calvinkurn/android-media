package com.tokopedia.feedcomponent.view.decor

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ShopRecomItemDecoration(
    val space: Int = 26
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val last = parent.adapter?.itemCount ?: 0

        when (position) {
            0 -> outRect.left = 40
            last - 1 -> {
                outRect.left = space
                outRect.right = 40
            }
            else -> outRect.left = space
        }
    }

}