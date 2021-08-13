package com.tokopedia.home_component.decoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.home_component.R

class BannerChannelSingleItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        outRect.left = view.context.resources.getDimensionPixelSize(R.dimen.home_component_margin_default)
        outRect.right = view.context.resources.getDimensionPixelSize(R.dimen.home_component_margin_default)
    }
}
