package com.tokopedia.common_category.viewholders.shimmer

import androidx.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_category.R
import com.tokopedia.common_category.model.shimmer.GridListShimmerModel


class GridListShimmerViewHolder(itemView: View) : AbstractViewHolder<GridListShimmerModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.item_grid_list_shimmer_view
    }

    override fun bind(element: GridListShimmerModel?) {
        itemView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}