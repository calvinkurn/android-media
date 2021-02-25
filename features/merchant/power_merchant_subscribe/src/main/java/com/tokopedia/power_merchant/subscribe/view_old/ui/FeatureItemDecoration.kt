package com.tokopedia.power_merchant.subscribe.view_old.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.setMargin

class FeatureItemDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemCount = state.itemCount
        val position = parent.getChildAdapterPosition(view)
        val viewHolder = parent.getChildViewHolder(view)
        val context = viewHolder.itemView.context

        val margin = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3).toInt()

        if(position == itemCount - 1) {
            viewHolder.itemView.setMargin(margin, 0, margin, 0)
        } else {
            viewHolder.itemView.setMargin(margin, 0, 0, 0)
        }
    }
}