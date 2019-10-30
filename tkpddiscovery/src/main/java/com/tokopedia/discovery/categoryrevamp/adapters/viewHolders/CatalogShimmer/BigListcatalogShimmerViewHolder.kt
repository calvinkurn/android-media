package com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer

import androidx.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.model.BigListCatalogShimmerModel


class BigListcatalogShimmerViewHolder(itemView: View) : AbstractViewHolder<BigListCatalogShimmerModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.item_big_list_shimmer_view
    }

    override fun bind(element: BigListCatalogShimmerModel?) {
        itemView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}