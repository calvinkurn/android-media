package com.tokopedia.tokomart.categorylist.presentation.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.setMargin

class TokoNowCategoryListDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = parent.getChildAdapterPosition(view)
        val viewHolder = parent.findViewHolderForLayoutPosition(itemPosition)
        val isFirstItem = itemPosition == 0

        if (isFirstItem) {
            viewHolder?.itemView?.setMargin(0, 0, 0, 0)
        }
    }
}