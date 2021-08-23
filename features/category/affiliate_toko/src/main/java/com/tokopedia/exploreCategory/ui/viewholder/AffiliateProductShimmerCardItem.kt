package com.tokopedia.exploreCategory.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate_toko.R
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateProductShimmerVHViewModel

class AffiliateProductShimmerCardItem(itemView: View)
    : AbstractViewHolder<AffiliateProductShimmerVHViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_product_card_shimmer_item_layout
    }

    override fun bind(element: AffiliateProductShimmerVHViewModel?) {

    }
}