package com.tokopedia.catalog.viewholder.shimmer

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.model.datamodel.CatalogStaggeredShimmerModel

class CatalogStaggeredShimmerCardItemVH(itemView: View)
    : AbstractViewHolder<CatalogStaggeredShimmerModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.catalog_staggered_shimmer_item_layout
    }

    override fun bind(element: CatalogStaggeredShimmerModel?) {

    }
}
