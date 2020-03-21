package com.tokopedia.gamification.giftbox.presentation.helpers

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CouponItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val topSpace = parent.dpToPx(36).toInt()
        if (parent.layoutManager?.getPosition(view) == 0) {
            outRect.left = topSpace
        }
        outRect.right = topSpace
    }
}