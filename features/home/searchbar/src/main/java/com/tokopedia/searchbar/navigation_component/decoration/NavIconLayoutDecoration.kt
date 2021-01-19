package com.tokopedia.searchbar.navigation_component.decoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.searchbar.R

class NavIconLayoutDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = view.context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_2)
        }

        if (parent.getChildAdapterPosition(view) == state.itemCount-1) {
            outRect.right = view.context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_2)
        }
    }
}
