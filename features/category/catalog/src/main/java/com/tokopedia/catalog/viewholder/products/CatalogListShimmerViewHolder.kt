package com.tokopedia.catalog.viewholder.products

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_category.R

class CatalogListShimmerViewHolder(itemView: View) : AbstractViewHolder<CatalogListShimmerModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.item_list_shimmer_view
    }

    override fun bind(element: CatalogListShimmerModel?) {
        itemView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}