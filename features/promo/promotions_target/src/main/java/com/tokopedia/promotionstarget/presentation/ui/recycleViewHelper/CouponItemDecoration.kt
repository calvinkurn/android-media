package com.tokopedia.promotionstarget.presentation.ui.recycleViewHelper

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promotionstarget.presentation.dpToPx

class CouponItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val topSpace = parent.dpToPx(12).toInt()
        if (parent.layoutManager?.getPosition(view) == 0) {
            outRect.left = topSpace
        }
        outRect.right = topSpace
    }
}