package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShimmerModel
import com.tokopedia.affiliate_toko.R

class AffiliateProductShimmerCardItemVH(itemView: View) :
    AbstractViewHolder<AffiliateShimmerModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_product_card_shimmer_item_layout
    }

    override fun bind(element: AffiliateShimmerModel?) = Unit
}
