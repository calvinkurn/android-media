package com.tokopedia.home_wishlist.view.custom

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceBottomItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount ?: 0) - 1) {
            val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70f, view.resources.displayMetrics).toInt()
            outRect.bottom = margin
        } else {
            outRect.bottom = 0
        }
    }
}