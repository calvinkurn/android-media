package com.tokopedia.home_wishlist.view.custom

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class SpaceBottomItemDecoration(val staggeredGridLayoutManager: StaggeredGridLayoutManager) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//        val position = parent.getChildAdapterPosition(view)
//
//        val isLast = position == state.itemCount - 1
//
//        if (isLast) {
//            outRect.bottom = margin
//        } else if(outRect.bottom != 0){
//            outRect.bottom = 0
//        }
        if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount ?: 0) - 1) {
            val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70f, view.resources.displayMetrics).toInt()
            outRect.bottom = margin
        } else {
            outRect.bottom = 0
        }
    }
}