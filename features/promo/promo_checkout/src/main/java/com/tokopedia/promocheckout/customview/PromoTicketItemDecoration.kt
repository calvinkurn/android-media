package com.tokopedia.promocheckout.customview

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class PromoTicketItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                left = spaceHeight
                right = spaceHeight-4
            }
            right = spaceHeight - 4
        }
    }
}