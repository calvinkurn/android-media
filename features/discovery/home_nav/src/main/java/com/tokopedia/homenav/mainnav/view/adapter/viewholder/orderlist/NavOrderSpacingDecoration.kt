package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class NavOrderSpacingDecoration(private val spacingBetween: Int, private val edgeMargin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        val pos = parent.getChildAdapterPosition(view)

        outRect.top = 0
        outRect.left = if (pos == 0) edgeMargin else spacingBetween / 2
        outRect.right = if (pos == parent.adapter!!.itemCount - 1) edgeMargin else spacingBetween / 2
        outRect.bottom = 0
    }
}
