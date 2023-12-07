package com.tokopedia.home_component.decoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.util.toDpInt

class BannerChannelDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = view.context.resources.getDimensionPixelSize(home_componentR.dimen.home_component_padding_horizontal_default)
        }

        outRect.right = 8f.toDpInt()
    }
}
