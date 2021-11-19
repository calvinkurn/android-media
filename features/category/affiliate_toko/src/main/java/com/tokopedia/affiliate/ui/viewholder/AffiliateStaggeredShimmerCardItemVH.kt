package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateStaggeredShimmerModel
import com.tokopedia.affiliate_toko.R

class AffiliateStaggeredShimmerCardItemVH(itemView: View)
    : AbstractViewHolder<AffiliateStaggeredShimmerModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_staggered_shimmer_item_layout
    }

    override fun bind(element: AffiliateStaggeredShimmerModel?) {

    }
}
