package com.tokopedia.tokomart.searchcategory.presentation.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R

class BannerComponentSingleItemDecoration: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        outRect.left = view.context.resources.getDimensionPixelSize(R.dimen.home_component_margin_default)
        outRect.right = view.context.resources.getDimensionPixelSize(R.dimen.home_component_margin_default)
    }
}