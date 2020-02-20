package com.tokopedia.home_wishlist.view.custom

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, view.resources.displayMetrics).toInt()
        val isLast = position == state.itemCount - 1
        if (isLast) {
            outRect.right = margin
        }
        if (position == 0) {
            outRect.left = margin
        }
    }
}