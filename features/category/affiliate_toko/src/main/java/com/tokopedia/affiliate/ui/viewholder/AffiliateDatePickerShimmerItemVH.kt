package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShimmerViewModel
import com.tokopedia.affiliate_toko.R
class AffiliateDatePickerShimmerItemVH(itemView: View) : AbstractViewHolder<AffiliateShimmerViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_date_filter_shimmer_item_layout
    }

    override fun bind(element: AffiliateShimmerViewModel?) = Unit
}
