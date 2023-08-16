package com.tokopedia.catalogcommon.util.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val startMargin: Int, private val endMargin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (position == 0) {
            outRect.left = startMargin
            outRect.right = 0
        } else if (position == parent.adapter?.itemCount?.minus(1)) {
            outRect.left = 0
            outRect.right = endMargin
        } else {
            outRect.left = 0
            outRect.right = 0
        }
    }
}
