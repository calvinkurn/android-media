package com.tokopedia.home_component.decoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.home_component.R
import com.tokopedia.kotlin.extensions.view.toDp

class SimpleHorizontalLinearLayoutDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = 12f.toDp().toInt()
        }

        if (parent.getChildAdapterPosition(view) == state.itemCount-1) {
            outRect.right = 12f.toDp().toInt()
        }
    }
}
