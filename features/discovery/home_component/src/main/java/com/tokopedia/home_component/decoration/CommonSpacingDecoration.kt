package com.tokopedia.home_component.decoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class CommonSpacingDecoration(
        val margin: Int = 0) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.left = margin
        }

        if (parent.getChildAdapterPosition(view) == state.itemCount-1) {
            outRect.right = margin
        }
    }
}
