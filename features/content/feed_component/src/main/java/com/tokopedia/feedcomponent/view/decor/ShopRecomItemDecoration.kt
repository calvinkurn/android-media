package com.tokopedia.feedcomponent.view.decor

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ShopRecomItemDecoration : RecyclerView.ItemDecoration() {

    companion object {
        private const val FIRST_ITEM = 0
        private const val SPACE_SHOP = 40
        private const val SPACE_EDGE = 26
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val last = parent.adapter?.itemCount ?: 0

        when (position) {
            FIRST_ITEM -> outRect.left = SPACE_SHOP
            last - 1 -> {
                outRect.left = SPACE_EDGE
                outRect.right = SPACE_SHOP
            }
            else -> outRect.left = SPACE_EDGE
        }
    }

}