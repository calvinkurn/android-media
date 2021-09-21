package com.tokopedia.home_component.decoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class CommonMarginStartDecoration(
        val marginStart: Int = 0,
        val marginEnd: Int = 0) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = marginStart
        }

        if (parent.getChildAdapterPosition(view) == state.itemCount-1) {
            outRect.right = marginEnd
        }
    }
}
