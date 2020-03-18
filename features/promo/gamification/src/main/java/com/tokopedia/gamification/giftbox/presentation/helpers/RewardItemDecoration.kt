package com.tokopedia.gamification.giftbox.presentation.helpers

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RewardItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        //todo Rahul later use dpToPx
//        val topSpace = parent.dpToPx(12).toInt()
        val topSpace = 12
        if (parent.layoutManager?.getPosition(view) != 0) {
            outRect.top = topSpace
        }
    }
}