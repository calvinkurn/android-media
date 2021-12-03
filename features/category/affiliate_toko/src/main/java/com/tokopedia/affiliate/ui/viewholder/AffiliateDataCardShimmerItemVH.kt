package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDataPlatformShimmerModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShimmerModel

class AffiliateDataCardShimmerItemVH(itemView: View)
    : AbstractViewHolder<AffiliateDataPlatformShimmerModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_adc_grid_shimmer
    }

    override fun bind(element: AffiliateDataPlatformShimmerModel?) {

    }
}
