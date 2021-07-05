package com.tokopedia.home_component.decoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.home_component.R

class BannerChannelDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = view.context.resources.getDimensionPixelSize(R.dimen.home_component_margin_default)
        }

        outRect.right = view.context.resources.getDimensionPixelSize(R.dimen.dp_8)
    }
}
