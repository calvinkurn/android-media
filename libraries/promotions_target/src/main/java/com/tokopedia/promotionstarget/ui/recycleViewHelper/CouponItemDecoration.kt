package com.tokopedia.promotionstarget.ui.recycleViewHelper

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.promotionstarget.dpToPx

class CouponItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val topSpace = parent.dpToPx(12).toInt()
        if (parent.layoutManager?.getPosition(view) == 0) {
            outRect.left = topSpace
        }
        outRect.right = topSpace
    }
}