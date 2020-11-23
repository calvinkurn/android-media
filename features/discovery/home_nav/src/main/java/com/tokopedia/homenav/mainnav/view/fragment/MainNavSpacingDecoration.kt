package com.tokopedia.homenav.mainnav.view.fragment

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class MainNavSpacingDecoration(private val edgeMargin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        val pos = parent.getChildAdapterPosition(view)

        outRect.top = 0
        outRect.right = 0
        val bottomValue = if (pos == (parent.adapter?.itemCount?:0) - 1) edgeMargin else 0
        outRect.bottom = bottomValue

        outRect.left = 0
    }
}
