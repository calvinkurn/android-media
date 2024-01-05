package com.tokopedia.home_component.decoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.home_component.R as home_componentR

class BannerChannelSingleItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        outRect.left = view.context.resources.getDimensionPixelSize(home_componentR.dimen.home_component_padding_horizontal_default)
        outRect.right = view.context.resources.getDimensionPixelSize(home_componentR.dimen.home_component_padding_horizontal_default)
    }
}
