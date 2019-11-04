package com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer

import androidx.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.model.ListCatalogShimmerModel


class ListCatalogShimmerViewHolder(itemView: View) : AbstractViewHolder<ListCatalogShimmerModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.item_list_shimmer_view
    }

    override fun bind(element: ListCatalogShimmerModel?) {
        itemView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}